package com.TaskCollab.Decorator;

import com.TaskCollab.Entity.TaskInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ValidationTaskDecoratorTest {

    private ValidationTaskDecorator validationTaskDecorator;

    @Mock
    private TaskInterface decoratedTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validationTaskDecorator = new ValidationTaskDecorator(decoratedTask);
    }

    @Test
    void testSetTask_TitleValid() {
        validationTaskDecorator.setTask_Title("Valid Title");
        verify(decoratedTask).setTask_Title("Valid Title");
    }

    @Test
    void testSetTask_TitleNull() {
        assertThrows(IllegalArgumentException.class, () -> validationTaskDecorator.setTask_Title(null));
        verify(decoratedTask, never()).setTask_Title(anyString());
    }

    @Test
    void testSetTask_TitleEmpty() {
        assertThrows(IllegalArgumentException.class, () -> validationTaskDecorator.setTask_Title(""));
        verify(decoratedTask, never()).setTask_Title(anyString());
    }

    @Test
    void testSetDescriptionValid() {
        validationTaskDecorator.setDescription("Valid Description");
        verify(decoratedTask).setDescription("Valid Description");
    }

    @Test
    void testSetDescriptionNull() {
        assertThrows(IllegalArgumentException.class, () -> validationTaskDecorator.setDescription(null));
        verify(decoratedTask, never()).setDescription(anyString());
    }

    @Test
    void testSetDescriptionEmpty() {
        assertThrows(IllegalArgumentException.class, () -> validationTaskDecorator.setDescription(""));
        verify(decoratedTask, never()).setDescription(anyString());
    }

    @Test
    void testSetDeadlineValid() {
        LocalDateTime now = LocalDateTime.now();
        validationTaskDecorator.setDeadline(now);
        verify(decoratedTask).setDeadline(now);
    }

    @Test
    void testSetDeadlineNull() {
        assertThrows(IllegalArgumentException.class, () -> validationTaskDecorator.setDeadline(null));
        verify(decoratedTask, never()).setDeadline(any());
    }

    @Test
    void testSetStatusValid() {
        validationTaskDecorator.setStatus("Valid Status");
        verify(decoratedTask).setStatus("Valid Status");
    }

    @Test
    void testSetStatusNull() {
        assertThrows(IllegalArgumentException.class, () -> validationTaskDecorator.setStatus(null));
        verify(decoratedTask, never()).setStatus(anyString());
    }

    @Test
    void testSetStatusEmpty() {
        assertThrows(IllegalArgumentException.class, () -> validationTaskDecorator.setStatus(""));
        verify(decoratedTask, never()).setStatus(anyString());
    }
}
