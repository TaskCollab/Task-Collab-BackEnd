package com.TaskCollab.Entity;

import org.junit.jupiter.api.Test;

import com.TaskCollab.Entity.Task;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskEntity() {
        Task task = new Task();
        task.setTask_id(1L);
        task.setTask_Title("Test Task");
        task.setDescription("This is a test description.");
        task.setAssigned_To(100L);
        task.setStatus("In Progress");
        LocalDateTime deadline = LocalDateTime.of(2025, 3, 15, 12, 0);
        task.setDeadline(deadline);

        assertEquals(1L, task.getTask_id());
        assertEquals("Test Task", task.getTask_Title());
        assertEquals("This is a test description.", task.getDescription());
        assertEquals(100L, task.getAssigned_To());
        assertEquals("In Progress", task.getStatus());
        assertEquals(deadline, task.getDeadline());
    }
}
