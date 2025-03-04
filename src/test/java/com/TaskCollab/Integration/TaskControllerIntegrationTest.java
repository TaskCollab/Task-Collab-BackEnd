package com.TaskCollab.Integration;

import com.TaskCollab.dto.TaskDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateAndDeleteTask() {
        // Prepare a new task payload
        TaskDTO newTask = new TaskDTO();
        newTask.setTaskTitle("Integration Test Task");
        newTask.setDescription("Task created for integration testing.");
        newTask.setAssignedTo(1L);
        newTask.setStatus("Pending");
        newTask.setDeadline(LocalDateTime.now().plusDays(5));

        // Create the task using POST /api/tasks
        ResponseEntity<TaskDTO> postResponse = restTemplate.postForEntity("/api/tasks", newTask, TaskDTO.class);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        TaskDTO createdTask = postResponse.getBody();
        assertNotNull(createdTask);
        assertNotNull(createdTask.getId());

        // Delete the task using DELETE /api/tasks/{id}
        Long taskId = createdTask.getId();
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/api/tasks/" + taskId, HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertEquals("Task deleted successfully.", deleteResponse.getBody());

        // Verify deletion by trying to GET the task and expecting NOT_FOUND
        ResponseEntity<TaskDTO> getResponse = restTemplate.getForEntity("/api/tasks/" + taskId, TaskDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}
