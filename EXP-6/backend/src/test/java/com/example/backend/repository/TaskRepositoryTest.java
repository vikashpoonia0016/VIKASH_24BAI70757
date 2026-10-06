package com.example.backend.repository;

import com.example.backend.entity.Comment;
import com.example.backend.entity.Task;
import com.example.backend.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void findAllSupportsPaginationAndSorting() {
        taskRepository.save(new Task("alpha", false));
        taskRepository.save(new Task("bravo", false));
        taskRepository.save(new Task("charlie", false));

        Page<Task> page = taskRepository.findAll(PageRequest.of(0, 2, Sort.by("title").ascending()));

        assertThat(page.getContent()).extracting(Task::getTitle).containsExactly("alpha", "bravo");
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void findAllWithCommentsLoadsAssociatedComments() {
        Task task = new Task("review", false);
        task.addComment(new Comment("first note"));
        task.addComment(new Comment("second note"));
        taskRepository.saveAndFlush(task);

        List<Task> tasks = taskRepository.findAllWithComments();

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getComments()).extracting(Comment::getText)
                .containsExactlyInAnyOrder("first note", "second note");
        assertThat(tasks.get(0).getComments()).allSatisfy(comment -> assertThat(comment.getTask()).isSameAs(tasks.get(0)));
    }

    @Test
    void findTop5TasksNativeReturnsNewestFiveInDescendingIdOrder() {
        List<Task> inserted = LongStream.rangeClosed(1, 7)
            .mapToObj(number -> taskRepository.save(new Task("task-" + number, false)))
            .toList();

        List<Task> topTasks = taskRepository.findTop5TasksNative();

        assertThat(topTasks).hasSize(5);
        assertThat(topTasks).extracting(Task::getId)
            .containsExactlyElementsOf(inserted.subList(2, inserted.size()).reversed().stream().map(Task::getId).toList());
    }
}