package com.TaskCollab.Decorator;

import com.TaskCollab.Entity.TaskInterface;

public class LoggingTaskDecorator extends TaskDecorator {

    public LoggingTaskDecorator(TaskInterface decoratedTask) {
        super(decoratedTask);
    }

    @Override
    public void setTask_Title(String task_Title) {
        System.out.println("Logging: Setting task title to " + task_Title);
        super.setTask_Title(task_Title);
    }

    @Override
    public void setStatus(String status) {
        System.out.println("Logging: Setting status to " + status);
        super.setStatus(status);
    }
    
}