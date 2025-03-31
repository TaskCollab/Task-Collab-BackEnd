package com.TaskCollab.dto;

import com.TaskCollab.Entity.TopUserInterface;

public class TopUsersDTO implements TopUserInterface{
    private String userName;
    private Long createcount;
    private Long updatecount;
    private Long deletecount;
    private Long readcount;

    // The total number of roles for all permission types.
    private Long totalcount;

    @Override
    public String getUserName() {
        return userName;
    }
    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Override
    public Long getCreatecount() {
        return createcount;
    }
    @Override
    public void setCreatecount(Long createcount) {
        this.createcount = createcount;
    }
    @Override
    public Long getUpdatecount() {
        return updatecount;
    }
    @Override
    public void setUpdatecount(Long updatecount) {
        this.updatecount = updatecount;
    }
    @Override
    public Long getDeletecount() {
        return deletecount;
    }
    @Override
    public void setDeletecount(Long deletecount) {
        this.deletecount = deletecount;
    }
    @Override
    public Long getReadcount() {
        return readcount;
    }
    @Override
    public void setReadcount(Long readcount) {
        this.readcount = readcount;
    }

    @Override
    public Long getTotalcount() {
        return totalcount;
    }

    @Override
    public void setTotalcount(Long totalcount) {
        this.totalcount = totalcount;
    }
}
