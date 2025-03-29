package com.TaskCollab.Controller;

import com.TaskCollab.config.JwtUtils;
import com.TaskCollab.dto.LoginRequest;
import com.TaskCollab.dto.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException; //Import AuthenticationException
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.TaskCollab.Service.AuthService; // Import the AuthService

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService; // Use AuthService mock

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("password");

        LoginResponse loginResponse = new LoginResponse("testToken");
        when(authService.authenticateAndGenerateToken(loginRequest)).thenReturn(loginResponse);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testToken", ((LoginResponse) response.getBody()).getToken());
    }

    @Test
    void testLoginFailureBadCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("wrongPassword");

        when(authService.authenticateAndGenerateToken(loginRequest)).thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authentication failed: Invalid credentials", response.getBody());
    }

    @Test
    void testLoginFailureGeneralAuthenticationException() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("wrongPassword");

        when(authService.authenticateAndGenerateToken(loginRequest)).thenThrow(new AuthenticationException("General Authentication Failure") {});

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authentication failed: General Authentication Failure", response.getBody());
    }

    @Test
    void testLoginFailureOtherException() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("wrongPassword");

        when(authService.authenticateAndGenerateToken(loginRequest)).thenThrow(new RuntimeException("Something unexpected happened"));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occured during login.", response.getBody());
    }
}