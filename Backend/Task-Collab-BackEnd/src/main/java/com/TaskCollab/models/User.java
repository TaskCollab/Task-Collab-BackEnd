package com.TaskCollab.models;

public class User {
    private String userId;
    private String username;
    private String password;
    private Boolean isAdmin;

    public User(String userId, String username, String password, Boolean isAdmin) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getDetails() {
        return "User ID: " + userId + ", Username: " + username + ", Admin: " + isAdmin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }
}
