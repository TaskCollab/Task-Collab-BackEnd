package com.TaskCollab.Entity;

public interface ActiveUserInterface {

    // We define the active users is the one who has the most completed task.
    public String getUserName();
    public void setUserName(String userName);
    public Long getCompletedCount();
    public void setCompletedCount(Long complete);
    public Long getTotalTaskCount();
    public void setTotalTaskCount(Long total);
    public Double getCompletedPercentage();
    public void setCompletedPercentage(Double percentage);
    
}
