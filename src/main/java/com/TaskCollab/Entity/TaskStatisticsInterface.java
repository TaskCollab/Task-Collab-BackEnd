package com.TaskCollab.Entity;

public interface TaskStatisticsInterface {
    // Getter methods
    public Long getCompletedTaskCount();
    public Long getPendingTaskCount();
    public Long getInProgressTaskCount();
    public Double getTotalTaskCount();
    public Double getCompletedPercentage();
    public Double getPendingPercentage();
    public Double getInProgressPercentage();

    // Setter methods
    public void setCompletedTaskCount(Long completed);
    public void setPendingTaskCount(Long pending);
    public void setTotalTaskCount(double total);
    public void setInProgressTaskCount(Long inProgress);
    public void setCompletedPercentage(double d);
    public void setPendingPercentage(double d);
    public void setInProgressPercentage(double d);
}
