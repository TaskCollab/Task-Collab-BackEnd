package com.TaskCollab.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class LoginResponseTest {

    @Test
    void constructorAndGetter_ShouldSetAndReturnToken() {
        // Arrange
        String expectedToken = "test.jwt.token";
        
        // Act
        LoginResponse response = new LoginResponse(expectedToken);
        
        // Assert
        assertThat(response.getToken()).isEqualTo(expectedToken);
    }

    @Test
    void setter_ShouldUpdateTokenValue() {
        // Arrange
        String initialToken = "initial.token";
        String updatedToken = "updated.token";
        LoginResponse response = new LoginResponse(initialToken);
        
        // Act
        response.setToken(updatedToken);
        
        // Assert
        assertThat(response.getToken()).isEqualTo(updatedToken);
    }

    @Test
    void shouldHandleNullToken() {
        // Arrange & Act
        LoginResponse response = new LoginResponse(null);
        response.setToken(null);
        
        // Assert
        assertThat(response.getToken()).isNull();
    }

    @Test
    void shouldHandleEmptyToken() {
        // Arrange & Act
        LoginResponse response = new LoginResponse("");
        response.setToken("");
        
        // Assert
        assertThat(response.getToken()).isEmpty();
    }

    @Test
    void shouldHandleSpecialCharactersInToken() {
        // Arrange
        String specialToken = "token~!@#$%^&*()_+{}|:\"<>?";
        
        // Act
        LoginResponse response = new LoginResponse(specialToken);
        
        // Assert
        assertThat(response.getToken()).isEqualTo(specialToken);
    }
}