package com.TaskCollab.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class LoginResponseTest {
    @Test
    void constructorAndGetter_ShouldWorkCorrectly() {
        LoginResponse response = new LoginResponse("test.token.123");
        assertEquals("test.token.123", response.getToken());
    }

    @Test
    void setter_ShouldUpdateTokenValue() {
        LoginResponse response = new LoginResponse("initial.token");
        response.setToken("updated.token");
        
        assertEquals("updated.token", response.getToken());
    }
}