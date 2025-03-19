package com.TaskCollab.Decorator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.TaskCollab.Entity.RoleInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidationRoleDecoratorTest {
    
    private RoleInterface roleMock;
    private ValidationRoleDecorator validationDecorator;
    
    @BeforeEach
    public void setUp() {
        // Create a mock for the decorated RoleInterface
        roleMock = mock(RoleInterface.class);
        // Wrap the mock in the ValidationRoleDecorator
        validationDecorator = new ValidationRoleDecorator(roleMock);
    }
    
    @Test
    public void testSetRoleName_Valid() {
        // Given a valid role name
        String validRoleName = "Admin";
        
        // When setRoleName is called with a valid name,
        // it should delegate the call to the decorated role.
        validationDecorator.setRoleName(validRoleName);
        
        // Verify the decorated role's setRoleName was called exactly once.
        verify(roleMock, times(1)).setRoleName(validRoleName);
    }
    
    @Test
    public void testSetRoleName_NullThrowsException() {
        // When setRoleName is called with null,
        // then an IllegalArgumentException is expected.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validationDecorator.setRoleName(null);
        });
        
        // Optionally, verify the exception message.
        assertEquals("Role name cannot be empty", exception.getMessage());
    }
    
    @Test
    public void testSetRoleName_EmptyThrowsException() {
        // When setRoleName is called with an empty string,
        // then an IllegalArgumentException is expected.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validationDecorator.setRoleName("");
        });
        
        // Verify the exception message.
        assertEquals("Role name cannot be empty", exception.getMessage());
    }
}
