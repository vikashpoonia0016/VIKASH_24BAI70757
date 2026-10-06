package com.example.backend.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskEntityTest {

    @Test
    void constructorInitializesTaskFieldsAndComments() {
        Task task = new Task("prepare lab", true);

        assertEquals("prepare lab", task.getTitle());
        assertTrue(task.isCompleted());
        assertTrue(task.getComments().isEmpty());
    }

    @Test
    void settersUpdateTaskFields() {
        Task task = new Task();
        task.setId(7L);
        task.setTitle("submit report");
        task.setCompleted(true);

        assertEquals(7L, task.getId());
        assertEquals("submit report", task.getTitle());
        assertTrue(task.isCompleted());
    }

    @Test
    void addCommentUpdatesBothSidesOfRelationship() {
        Task task = new Task("prepare lab", false);
        Comment comment = new Comment("bring equipment");

        task.addComment(comment);

        assertEquals(1, task.getComments().size());
        assertSame(comment, task.getComments().get(0));
        assertSame(task, comment.getTask());
    }

    @Test
    void removeCommentUpdatesBothSidesOfRelationship() {
        Task task = new Task("prepare lab", false);
        Comment comment = new Comment("bring equipment");
        task.addComment(comment);

        task.removeComment(comment);

        assertTrue(task.getComments().isEmpty());
        assertNull(comment.getTask());
    }

    @Test
    void commentConstructorAndSettersManageFields() {
        Comment comment = new Comment("review notes");
        Task task = new Task("review", false);
        comment.setId(11L);
        comment.setText("updated notes");
        comment.setTask(task);

        assertEquals(11L, comment.getId());
        assertEquals("updated notes", comment.getText());
        assertSame(task, comment.getTask());
    }

    @Test
    void defaultTaskIsNotCompleted() {
        assertFalse(new Task().isCompleted());
    }
}