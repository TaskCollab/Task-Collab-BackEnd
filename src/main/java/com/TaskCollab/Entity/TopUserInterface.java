package com.TaskCollab.Entity;

public interface TopUserInterface {
    
    public String getUserName();
    public void setUserName(String userName);
    public Long getCreatecount();
    public void setCreatecount(Long createcount);
    public Long getUpdatecount();
    public void setUpdatecount(Long updatecount);
    public Long getDeletecount();
    public void setDeletecount(Long deletecount);
    public Long getReadcount(); 
    public void setReadcount(Long readcount);
    public Long getTotalcount();
    public void setTotalcount(Long totalcount);
}
