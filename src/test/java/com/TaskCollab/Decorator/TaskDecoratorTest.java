package com.TaskCollab.Decorator;

import com.TaskCollab.Entity.TaskInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskDecoratorTest {

    private TaskDecorator taskDecorator;

    @Mock
    private TaskInterface decoratedTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskDecorator = new TaskDecorator(decoratedTask) {}; // Anonymous subclass for testing
    }

    @Test
    void testGetTask_Id() {
        when(decoratedTask.getTask_Id()).thenReturn(1L);
        assertEquals(1L, taskDecorator.getTask_Id());
        verify(decoratedTask).getTask_Id();
    }

    @Test
    void testSetTask_Id() {
        taskDecorator.setTask_Id(1L);
        verify(decoratedTask).setTask_Id(1L);
    }

    @Test
    void testGetTask_Title() {
        when(decoratedTask.getTask_Title()).thenReturn("Test Title");
        assertEquals("Test Title", taskDecorator.getTask_Title());
        verify(decoratedTask).getTask_Title();
    }

    @Test
    void testSetTask_Title() {
        taskDecorator.setTask_Title("Test Title");
        verify(decoratedTask).setTask_Title("Test Title");
    }

    @Test
    void testGetDescription() {
        when(decoratedTask.getDescription()).thenReturn("Test Description");
        assertEquals("Test Description", taskDecorator.getDescription());
        verify(decoratedTask).getDescription();
    }

    @Test
    void testSetDescription() {
        taskDecorator.setDescription("Test Description");
        verify(decoratedTask).setDescription("Test Description");
    }

    @Test
    void testGetAssigned_To() {
        when(decoratedTask.getAssigned_To()).thenReturn("John Doe");
        assertEquals("John Doe", taskDecorator.getAssigned_To());
        verify(decoratedTask).getAssigned_To();
    }

    @Test
    void testSetAssigned_To() {
        taskDecorator.setAssigned_To("John Doe");
        verify(decoratedTask).setAssigned_To("John Doe");
    }

    @Test
    void testGetStatus() {
        when(decoratedTask.getStatus()).thenReturn("In Progress");
        assertEquals("In Progress", taskDecorator.getStatus());
        verify(decoratedTask).getStatus();
    }

    @Test
    void testSetStatus() {
        taskDecorator.setStatus("In Progress");
        verify(decoratedTask).setStatus("In Progress");
    }

    @Test
    void testGetDeadline() {
        LocalDateTime now = LocalDateTime.now();
        when(decoratedTask.getDeadline()).thenReturn(now);
        assertEquals(now, taskDecorator.getDeadline());
        verify(decoratedTask).getDeadline();
    }

    @Test
    void testSetDeadline() {
        LocalDateTime now = LocalDateTime.now();
        taskDecorator.setDeadline(now);
        verify(decoratedTask).setDeadline(now);
    }
}
