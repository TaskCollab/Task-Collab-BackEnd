package com.TaskCollab.dto;

import com.TaskCollab.Entity.TaskStatisticsInterface;

public class TaskStatisticsDTO implements TaskStatisticsInterface {
    private Long completedTaskCount;
    private Long inProgressTaskCount;
    private Long pendingTaskCount;
    private Double totalTaskCount;
    private Double completedPercentage;
    private Double pendingPercentage;
    private Double inProgressPercentage;

    @Override
    public void setCompletedTaskCount(Long completed) {
        this.completedTaskCount = completed;
    }

    @Override
    public void setPendingTaskCount(Long pending) {
        this.pendingTaskCount = pending;
    }

    public void setTotalTaskCount(double total) {
        this.totalTaskCount = total;
    }

    public void setInProgressTaskCount(Long inProgress) {
        this.inProgressTaskCount = inProgress;
    }

    public void setCompletedPercentage(double d) {
        this.completedPercentage = d;
    }

    public void setPendingPercentage(double d) {
        this.pendingPercentage = d;
    }

    public void setInProgressPercentage(double d) {
        this.inProgressPercentage = d;
    }

    // Getter methods to enable Jackson serialization to JSON
    public Long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public Long getInProgressTaskCount() {
        return inProgressTaskCount;
    }

    public Long getPendingTaskCount() {
        return pendingTaskCount;
    }

    public Double getTotalTaskCount() {
        return totalTaskCount;
    }

    public Double getCompletedPercentage() {
        return completedPercentage;
    }

    public Double getPendingPercentage() {
        return pendingPercentage;
    }

    public Double getInProgressPercentage() {
        return inProgressPercentage;
    }
}
