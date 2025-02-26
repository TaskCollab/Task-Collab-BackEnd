package com.TaskCollab.Entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsersTest {

    private Users user;
    private Role testRole;

    @BeforeEach
    void setUp() {
        user = new Users();
        testRole = new Role();
        testRole.setRoleName("ADMIN");
    }

    @Test
    void testGettersAndSetters() {
        // Test userId
        user.setUserId(1L);
        assertEquals(1L, user.getUserId(), "User ID should match");

        // Test username
        user.setUsername("john_doe");
        assertEquals("john_doe", user.getUsername(), "Username should match");

        // Test password
        user.setPassword("securePass123!");
        assertEquals("securePass123!", user.getPassword(), "Password should match");

        // Test role
        user.setRole(testRole);
        assertSame(testRole, user.getRole(), "Role should be the same instance");
        assertEquals("ADMIN", user.getRole().getRoleName(), "Role name should match");

        // Test isAdmin
        user.setIsAdmin(true);
        assertTrue(user.getIsAdmin(), "Admin status should be true");
    }

    @Test
    void testDefaultValues() {
        assertNull(user.getUserId(), "Default user ID should be null");
        assertNull(user.getUsername(), "Default username should be null");
        assertNull(user.getPassword(), "Default password should be null");
        assertNull(user.getRole(), "Default role should be null");
        assertFalse(user.getIsAdmin(), "Default admin status should be false");
    }

    @Test
    void testEdgeCases() {
        // Test negative user ID
        user.setUserId(-1L);
        assertEquals(-1L, user.getUserId(), "Should handle negative user IDs");

        // Test empty username
        user.setUsername("");
        assertEquals("", user.getUsername(), "Should handle empty username");

        // Test long username
        String longUsername = "a".repeat(255);
        user.setUsername(longUsername);
        assertEquals(longUsername, user.getUsername(), "Should handle long usernames");

        // Test null role
        user.setRole(null);
        assertNull(user.getRole(), "Should handle null role");
    }

    @Test
    void testAdminStatusToggle() {
        // Test setting to true
        user.setIsAdmin(true);
        assertTrue(user.getIsAdmin(), "Admin status should be true after setting");

        // Test setting back to false
        user.setIsAdmin(false);
        assertFalse(user.getIsAdmin(), "Admin status should be false after reset");
    }

    @Test
    void testRoleRelationship() {
        Role newRole = new Role();
        newRole.setRoleName("USER");
        
        user.setRole(newRole);
        assertSame(newRole, user.getRole(), "Should return the set role instance");
        assertEquals("USER", user.getRole().getRoleName(), "Should reflect updated role name");
    }

    @Test
    void testSpecialCharactersInUsername() {
        user.setUsername("user@domain.com");
        assertEquals("user@domain.com", user.getUsername(), "Should handle special characters");

        user.setUsername("user_name-with-dash");
        assertEquals("user_name-with-dash", user.getUsername(), "Should handle hyphens and underscores");
    }
}