package com.TaskCollab.Decorator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.TaskCollab.Entity.RoleInterface;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

//This is the LoggingRoleDecoratorTest class testing the LoggingRoleDecorator class.
public class LoggingRoleDecoratorTest {

    // Capture System.out output
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testSetRoleName_delegatesAndLogs() {
        // Arrange: create a mock of RoleInterface
        RoleInterface mockRole = Mockito.mock(RoleInterface.class);
        // Create the decorator that wraps the mock
        LoggingRoleDecorator decorator = new LoggingRoleDecorator(mockRole);
        String roleName = "Admin";

        // Act: call setRoleName on the decorator
        decorator.setRoleName(roleName);

        // Assert:
        // 1. Verify that the underlying role's setRoleName() is called exactly once with the correct argument.
        Mockito.verify(mockRole, Mockito.times(1)).setRoleName(roleName);

        // 2. Verify that the expected logging output is printed.
        String output = outContent.toString().trim();
        String expectedLog = "Logging: Setting role name to " + roleName;
        assertTrue(output.contains(expectedLog), "Expected log output not found");
    }
}
