package com.example.backend.service;

import com.example.backend.entity.Task;
import com.example.backend.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository);
    }

    @Test
    void getTasksPaginatedDelegatesToRepository() {
        var pageable = PageRequest.of(0, 5);
        Page<Task> expected = Page.empty(pageable);
        when(taskRepository.findAll(pageable)).thenReturn(expected);

        Page<Task> actual = taskService.getTasksPaginated(pageable);

        assertSame(expected, actual);
        verify(taskRepository).findAll(pageable);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void getTop5TasksDelegatesToNativeQuery() {
        List<Task> expected = List.of(new Task("latest", false));
        when(taskRepository.findTop5TasksNative()).thenReturn(expected);

        assertSame(expected, taskService.getTop5Tasks());

        verify(taskRepository).findTop5TasksNative();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void getAllTasksWithCommentsDelegatesToRepository() {
        List<Task> expected = List.of(new Task("with comment", false));
        when(taskRepository.findAllWithComments()).thenReturn(expected);

        assertSame(expected, taskService.getAllTasksWithComments());

        verify(taskRepository).findAllWithComments();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void addTaskSavesAndReturnsTask() {
        Task task = new Task("write tests", false);
        Task saved = new Task("write tests", false);
        when(taskRepository.save(task)).thenReturn(saved);

        assertSame(saved, taskService.addTask(task));

        verify(taskRepository).save(task);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void deleteTaskDeletesById() {
        taskService.deleteTask(42L);

        verify(taskRepository).deleteById(42L);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void toggleTaskFlipsCompletionAndSavesTask() {
        Task task = new Task("finish report", false);
        when(taskRepository.findById(42L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updated = taskService.toggleTask(42L);

        assertSame(task, updated);
        assertTrue(updated.isCompleted());
        verify(taskRepository).findById(42L);
        verify(taskRepository).save(task);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void toggleTaskTurnsCompletedTaskOff() {
        Task task = new Task("finish report", true);
        when(taskRepository.findById(42L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updated = taskService.toggleTask(42L);

        assertFalse(updated.isCompleted());
        verify(taskRepository).findById(42L);
        verify(taskRepository).save(task);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void toggleTaskReturnsNullWhenTaskDoesNotExist() {
        when(taskRepository.findById(42L)).thenReturn(Optional.empty());

        assertNull(taskService.toggleTask(42L));

        verify(taskRepository).findById(42L);
        verifyNoMoreInteractions(taskRepository);
    }
}