package com.TaskCollab.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "task")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_Id")
    private Long task_Id;

    private String task_Title;
    private String description;
    private String assigned_To;
    private String status;
    private LocalDateTime deadline;

    // Getters and Setters
    public Long getTask_id() {
        return task_Id;
    }

    public void setTask_id(Long task_id) {
        this.task_Id = task_id;
    }

    public String getTask_Title() {
        return task_Title;
    }

    public void setTask_Title(String task_Title) {
        this.task_Title = task_Title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssigned_To() {
        return assigned_To;
    }

    public void setAssigned_To(String assigned_To) {
        this.assigned_To = assigned_To;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
