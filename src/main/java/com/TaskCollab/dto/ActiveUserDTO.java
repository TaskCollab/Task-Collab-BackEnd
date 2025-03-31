package com.TaskCollab.dto;

import com.TaskCollab.Entity.ActiveUserInterface;

public class ActiveUserDTO implements ActiveUserInterface{

    String username;
    Long completedTaskCount;
    Long totalTaskCount;
    Double percentage;

    @Override
    public String getUserName(){
        return username;
    }

    @Override
    public void setUserName(String userName){
        this.username = userName;
    }

    @Override
    public Long getCompletedCount(){
        return completedTaskCount; 
    }

    @Override
    public void setCompletedCount(Long complete){
        this.completedTaskCount = complete;
    }

    @Override
    public Long getTotalTaskCount(){
        return totalTaskCount;
    }

    @Override
    public void setTotalTaskCount(Long total){
        this.totalTaskCount = total;
    }

    @Override
    public Double getCompletedPercentage(){
        return percentage;
    }

    @Override
    public void setCompletedPercentage(Double percentage){
        this.percentage = percentage;
    }
    
}
