package com.TaskCollab.Decorator;

import com.TaskCollab.Entity.TaskInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class LoggingTaskDecoratorTest {

    private LoggingTaskDecorator loggingTaskDecorator;

    @Mock
    private TaskInterface decoratedTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loggingTaskDecorator = new LoggingTaskDecorator(decoratedTask);
    }

    @Test
    void testSetTask_Title() {
        loggingTaskDecorator.setTask_Title("Test Title");
        verify(decoratedTask).setTask_Title("Test Title");
        // Verify console output (if needed)
    }

    @Test
    void testSetStatus() {
        loggingTaskDecorator.setStatus("In Progress");
        verify(decoratedTask).setStatus("In Progress");
        // Verify console output (if needed)
    }
}