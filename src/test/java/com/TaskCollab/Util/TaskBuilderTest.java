package com.TaskCollab.Util;

import com.TaskCollab.Entity.Task;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TaskBuilderTest {
    
    @Test
    void testTaskBuilderCreatesTask() {
        String title = "Test Task";
        String description = "Test description";
        Long assignedTo = 1L;
        String status = "Pending";
        LocalDateTime deadline = LocalDateTime.now().plusDays(5);

        Task task = new TaskBuilder()
                .withTitle(title)
                .withDescription(description)
                .withAssignedTo(assignedTo)
                .withStatus(status)
                .withDeadline(deadline)
                .build();

        assertNotNull(task);
        assertEquals(title, task.getTask_Title());
        assertEquals(description, task.getDescription());
        assertEquals(assignedTo, task.getAssigned_To());
        assertEquals(status, task.getStatus());
        assertEquals(deadline, task.getDeadline());
    }
}
