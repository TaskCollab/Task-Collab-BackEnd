package com.TaskCollab.Model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import com.TaskCollab.Entity.Task;

class TaskTest {

    @Test
    void testTaskGettersAndSetters() {
        Task task = new Task();
        task.setTask_Id(1L);
        task.setTask_Title("Test Task");
        task.setDescription("Test Description");
        task.setAssigned_To("User1");
        task.setStatus("Pending");
        task.setDeadline(LocalDateTime.of(2024, 3, 10, 10, 30));

        assertEquals(1L, task.getTask_Id());
        assertEquals("Test Task", task.getTask_Title());
        assertEquals("Test Description", task.getDescription());
        assertEquals("User1", task.getAssigned_To());
        assertEquals("Pending", task.getStatus());
        assertEquals(LocalDateTime.of(2024, 3, 10, 10, 30), task.getDeadline());
    }
}
