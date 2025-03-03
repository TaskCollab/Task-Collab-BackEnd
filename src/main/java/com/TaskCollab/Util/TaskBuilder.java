package com.TaskCollab.Util;


import com.TaskCollab.Entity.Task;
import java.time.LocalDateTime;

public class TaskBuilder {
    private Task task;

    public TaskBuilder() {
        this.task = new Task();
    }

    public TaskBuilder withTitle(String title) {
        task.setTask_Title(title);
        return this;
    }

    public TaskBuilder withDescription(String description) {
        task.setDescription(description);
        return this;
    }

    public TaskBuilder withAssignedTo(Long assignedTo) {
        task.setAssigned_To(assignedTo);
        return this;
    }

    public TaskBuilder withStatus(String status) {
        task.setStatus(status);
        return this;
    }

    public TaskBuilder withDeadline(LocalDateTime deadline) {
        task.setDeadline(deadline);
        return this;
    }

    public Task build() {
        return task;
    }
}


    

