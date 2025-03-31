package com.TaskCollab.Controller;

import com.TaskCollab.Service.TaskService;
import com.TaskCollab.config.JwtProperties;
import com.TaskCollab.dto.TaskDTO;
import com.TaskCollab.Entity.TaskInterface;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper; // Added import

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private TaskController taskController;

    private ModelMapper modelMapper; // Added ModelMapper

    private TaskInterface mockTask;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper(); // Initialize ModelMapper
        taskController.modelMapper = modelMapper; //Inject modelmapper into the controller.

        mockTask = mock(TaskInterface.class);
    }


    @Test
    void testGetTask_Found() {
        // Arrange
        when(mockTask.getTask_Id()).thenReturn(100L);
        when(mockTask.getTask_Title()).thenReturn("Mock Task Title");
        when(taskService.getTaskById(100L)).thenReturn(mockTask);

        // Act
        ResponseEntity<TaskDTO> response = taskController.getTask(100L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getId());
        verify(taskService).getTaskById(100L);
    }

    @Test
    void testGetTask_NotFound() {
        // Arrange
        when(taskService.getTaskById(999L)).thenReturn(null);

        // Act
        ResponseEntity<TaskDTO> response = taskController.getTask(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(taskService).getTaskById(999L);
    }

    @Test
    void testUpdateTask_Success() {
        // Arrange
        when(mockTask.getTask_Id()).thenReturn(100L);
        when(taskService.updateTask(eq(123L), any(TaskDTO.class))).thenReturn(mockTask);

        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setTaskTitle("New Title");

        // Act
        ResponseEntity<TaskDTO> response = taskController.updateTask(123L, updateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getId()); // from mockTask
        verify(taskService).updateTask(eq(123L), any(TaskDTO.class));
    }

    @Test
    void testUpdateTask_NotFound() {
        // Arrange
        when(taskService.updateTask(eq(123L), any(TaskDTO.class))).thenReturn(null);

        // Act
        ResponseEntity<TaskDTO> response = taskController.updateTask(123L, new TaskDTO());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(taskService).updateTask(eq(123L), any(TaskDTO.class));
    }

    @Test
    void testCreateTask() {
        // Arrange
        when(mockTask.getTask_Title()).thenReturn("Mock Task Title");
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(mockTask);

        // Act
        ResponseEntity<TaskDTO> response = taskController.createTask(new TaskDTO());

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Mock Task Title", response.getBody().getTaskTitle());
        verify(taskService).createTask(any(TaskDTO.class));
    }

    @Test
    void testDeleteTask_Success() {
        // Arrange
        when(taskService.deleteTask(50L)).thenReturn(true);

        // Act
        ResponseEntity<String> response = taskController.deleteTask(50L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task deleted successfully.", response.getBody());
        verify(taskService).deleteTask(50L);
    }

    @Test
    void testDeleteTask_NotFound() {
        // Arrange
        when(taskService.deleteTask(60L)).thenReturn(false);

        // Act
        ResponseEntity<String> response = taskController.deleteTask(60L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(taskService).deleteTask(60L);
    }

    @Test
    void testGetMyTasks() {
        // Arrange
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer ABCDE123456");
        when(jwtProperties.getSecret()).thenReturn("MyTestSecret12345678901234567890");

        String realToken = Jwts.builder()
                .setSubject("mockUser")
                .signWith(Keys.hmacShaKeyFor("MyTestSecret12345678901234567890".getBytes()))
                .compact();
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + realToken);

        when(mockTask.getTask_Title()).thenReturn("Mock Task Title");
        when(taskService.getTasksByUsername("mockUser"))
                .thenReturn(Collections.singletonList(mockTask));

        // Act
        ResponseEntity<List<TaskDTO>> response = taskController.getMyTasks(mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Mock Task Title", response.getBody().get(0).getTaskTitle());

        verify(taskService).getTasksByUsername("mockUser");
    }
}