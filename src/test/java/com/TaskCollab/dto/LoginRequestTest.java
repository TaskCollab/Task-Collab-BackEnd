package com.TaskCollab.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginRequestTest {

    private Validator validator;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        loginRequest = new LoginRequest();
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Arrange & Act
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("securePass123!");

        // Assert
        assertThat(loginRequest.getUsername()).isEqualTo("testUser");
        assertThat(loginRequest.getPassword()).isEqualTo("securePass123!");
    }

    @Test
    void validation_ShouldPassWithValidData() {
        // Arrange
        loginRequest.setUsername("validUser");
        loginRequest.setPassword("validPassword");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_ShouldFailWithBlankUsername() {
        // Arrange
        loginRequest.setUsername("");
        loginRequest.setPassword("validPassword");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("Username is required");
    }

    @Test
    void validation_ShouldFailWithBlankPassword() {
        // Arrange
        loginRequest.setUsername("validUser");
        loginRequest.setPassword("");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("Password is required");
    }

    @Test
    void validation_ShouldFailWithBothFieldsBlank() {
        // Arrange
        loginRequest.setUsername("");
        loginRequest.setPassword("");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message")
            .containsExactlyInAnyOrder(
                "Username is required",
                "Password is required"
            );
    }

    @Test
    void validation_ShouldFailWithWhitespaceUsername() {
        // Arrange
        loginRequest.setUsername("   ");
        loginRequest.setPassword("validPassword");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("Username is required");
    }

    @Test
    void validation_ShouldFailWithWhitespacePassword() {
        // Arrange
        loginRequest.setUsername("validUser");
        loginRequest.setPassword("   ");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("Password is required");
    }
}