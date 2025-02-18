package com.TaskCollab.Entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

public class UserTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        
        // Test basic fields
        user.setUserId(1L);
        user.setUsername("john_doe");
        user.setPassword("securePass123!");
        user.setAdmin(true);

        assertEquals(1L, user.getUserId());
        assertEquals("john_doe", user.getUsername());
        assertEquals("securePass123!", user.getPassword());
        assertTrue(user.isAdmin());
    }

    @Test
    void testRolesManagement() {
        User user = new User();
        
        // Create test roles
        Role userRole = createTestRole(1, "USER");
        Role adminRole = createTestRole(2, "ADMIN");

        // Test initial state
        assertTrue(user.getRoles().isEmpty());

        // Test adding single role
        user.getRoles().add(userRole);
        assertEquals(1, user.getRoles().size());
        assertTrue(containsRoleWithId(user.getRoles(), 1));

        // Test replacing roles
        Set<Role> newRoles = new HashSet<>();
        newRoles.add(adminRole);
        user.setRoles(newRoles);
        
        assertEquals(1, user.getRoles().size());
        assertTrue(containsRoleWithId(user.getRoles(), 2));
    }

    @Test
    void testRoleRelationships() {
        User user = new User();
        Role testRole = createTestRole(3, "MANAGER");
        
        user.getRoles().add(testRole);
        assertTrue(containsRoleWithId(user.getRoles(), 3));
    }

    private Role createTestRole(int roleId, String roleName) {
        Role role = new Role();
        role.setRoleId(roleId);
        role.setRoleName(roleName);
        // Set default permissions if needed
        role.setCreate(false);
        role.setRead(true);
        role.setUpdate(false);
        role.setDelete(false);
        return role;
    }

    private boolean containsRoleWithId(Set<Role> roles, int roleId) {
        return roles.stream()
                .anyMatch(role -> role.getRoleId() == roleId);
    }
}