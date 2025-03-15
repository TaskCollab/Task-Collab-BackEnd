package com.TaskCollab.Service;

import com.TaskCollab.Entity.Task;
import com.TaskCollab.Entity.TaskInterface;
import com.TaskCollab.dao.TaskRepository;
import com.TaskCollab.dto.TaskDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTask_Id(1L);
        task.setTask_Title("Sample Task");
        task.setDescription("Sample Description");
        task.setAssigned_To("user1");
        task.setStatus("In Progress");
        task.setDeadline(LocalDateTime.now());
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        
        TaskInterface foundTask = taskService.getTaskById(1L);
        
        assertNotNull(foundTask);
        assertEquals("Sample Task", foundTask.getTask_Title());
    }

    @Test
    void testCreateTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskTitle("New Task");
        taskDTO.setDescription("New Description");
        taskDTO.setAssignedTo("user2");
        taskDTO.setStatus("Pending");
        taskDTO.setDeadline(LocalDateTime.now());

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskInterface createdTask = taskService.createTask(taskDTO);

        assertNotNull(createdTask);
        assertEquals("Sample Task", createdTask.getTask_Title());
    }

    @Test
    void testUpdateTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskTitle("Updated Task");
        taskDTO.setDescription("Updated Description");
        taskDTO.setAssignedTo("user3");
        taskDTO.setStatus("Completed");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskInterface updatedTask = taskService.updateTask(1L, taskDTO);

        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getTask_Title());
        assertEquals("Updated Description", updatedTask.getDescription());
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(1L);

        boolean isDeleted = taskService.deleteTask(1L);

        assertTrue(isDeleted);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetTasksByUsername() {
        when(taskRepository.findByAssigned_To("user1")).thenReturn(Arrays.asList(task));

        List<TaskInterface> tasks = taskService.getTasksByUsername("user1");

        assertEquals(1, tasks.size());
        assertEquals("Sample Task", tasks.get(0).getTask_Title());
    }
}
