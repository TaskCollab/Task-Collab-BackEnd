package com.TaskCollab.Entity;

public interface RoleStatisticsInterface {

    // Getter methods
    public Long getCreateRoleCount();
    public Long getReadRoleCount();
    public Long getUpdateRoleCount();
    public Long getDeleteRoleCount();
    public Long getTotalRoleCount();
    public Double getCreateRolePercentage();
    public Double getReadRolePercentage();
    public Double getUpdateRolePercentage();
    public Double getDeleteRolePercentage();

    // Setter methods
    public void setCreateRoleCount(Long create);
    public void setReadRoleCount(Long read);
    public void setUpdateRoleCount(Long update);
    public void setDeleteRoleCount(Long delete);
    public void setTotalRoleCount(Long total);
    public void setCreateRolePercentage(double d);
    public void setReadRolePercentage(double d);
    public void setUpdateRolePercentage(double d);
    public void setDeleteRolePercentage(double d);
    
}
