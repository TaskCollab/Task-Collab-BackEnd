package com.TaskCollab.Service;

import com.TaskCollab.Entity.Task;
import com.TaskCollab.dao.TaskRepository;
import com.TaskCollab.dto.TaskDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTaskById() {
        Task task = new Task();
        task.setTask_id(1L); 
        task.setTask_Title("Test Task");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Test Task", result.getTaskTitle());
    }

    @Test
    void testUpdateTask() {
        Task existingTask = new Task();
        existingTask.setTask_id(1L);
        existingTask.setTask_Title("Old Title");

        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setTaskTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setDeadline(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskDTO result = taskService.updateTask(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTaskTitle());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        TaskDTO result = taskService.getTaskById(99L);

        assertNull(result);
    }
}
