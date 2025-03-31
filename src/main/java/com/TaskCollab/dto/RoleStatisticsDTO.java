package com.TaskCollab.dto;

import com.TaskCollab.Entity.RoleStatisticsInterface;

public class RoleStatisticsDTO implements RoleStatisticsInterface {
    private Long createRoleCount;
    private Long readRoleCount;
    private Long updateRoleCount;
    private Long deleteRoleCount;
    private Long totalRoleCount;
    private Double createRolePercentage;
    private Double readRolePercentage;
    private Double updateRolePercentage;
    private Double deleteRolePercentage;

    // Getter methods
    @Override
    public Long getCreateRoleCount(){
        return createRoleCount;
    }

    @Override
    public Long getReadRoleCount(){
        return readRoleCount; 
    }

    @Override
    public Long getUpdateRoleCount(){
        return updateRoleCount; 
    }

    @Override
    public Long getDeleteRoleCount(){
        return deleteRoleCount;
    }

    @Override
    public Long getTotalRoleCount(){
        return totalRoleCount;
    }

    @Override
    public Double getCreateRolePercentage(){
        return createRolePercentage;
    }

    @Override
    public Double getReadRolePercentage(){
        return readRolePercentage;
    }

    @Override
    public Double getUpdateRolePercentage(){
        return updateRolePercentage;
    }

    @Override
    public Double getDeleteRolePercentage(){
        return deleteRolePercentage;
    }


    //Setter method
    @Override
    public void setCreateRoleCount(Long create){
        this.createRoleCount = create;
    }

    @Override
    public void setReadRoleCount(Long read){
        this.readRoleCount = read;
    }

    @Override
    public void setUpdateRoleCount(Long update){
        this.updateRoleCount = update;
    }

    @Override
    public void setDeleteRoleCount(Long delete){
        this.deleteRoleCount = delete;
    }

    @Override
    public void setTotalRoleCount (Long total){
        this.totalRoleCount = total;
    }

    @Override
    public void setCreateRolePercentage(double d){
        this.createRolePercentage = d;
    }

    @Override
    public void setReadRolePercentage(double d){
        this.readRolePercentage = d; 
    }

    @Override
    public void setUpdateRolePercentage(double d){
        this.updateRolePercentage = d;
    }

    @Override
    public void setDeleteRolePercentage(double d){
        this.deleteRolePercentage = d;
    }

}
