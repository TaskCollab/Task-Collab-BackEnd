package com.TaskCollab.dto;

public class LoginResponse {
    private String token;

    // Constructor
    public LoginResponse(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    // Setter 
    public void setToken(String token) {
        this.token = token;
    }
}