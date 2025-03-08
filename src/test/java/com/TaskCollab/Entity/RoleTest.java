package com.TaskCollab.Entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void testGettersAndSetters() {
        // Test roleId
        role.setRoleId(100);
        assertEquals(100, role.getRoleId(), "Role ID should match the set value");

        // Test roleName
        role.setRoleName("ADMIN");
        assertEquals("ADMIN", role.getRoleName(), "Role name should match the set value");

        // Test permissions
        role.setCreatePermission(true);
        assertTrue(role.isCreatePermission(), "Create permission should be true");

        role.setReadPermission(true);
        assertTrue(role.isReadPermission(), "Read permission should be true");

        role.setUpdatePermission(true);
        assertTrue(role.isUpdatePermission(), "Update permission should be true");

        role.setDeletePermission(true);
        assertTrue(role.isDeletePermission(), "Delete permission should be true");
    }

    @Test
    void testDefaultValues() {
        assertNull(role.getRoleId(), "Default role ID should be null");
        assertNull(role.getRoleName(), "Default role name should be null");
        assertFalse(role.isCreatePermission(), "Default create permission should be false");
        assertFalse(role.isReadPermission(), "Default read permission should be false");
        assertFalse(role.isUpdatePermission(), "Default update permission should be false");
        assertFalse(role.isDeletePermission(), "Default delete permission should be false");
    }

    @Test
    void testEdgeCases() {
        // Test negative role ID
        role.setRoleId(-1);
        assertEquals(-1, role.getRoleId(), "Should handle negative role IDs");

        // Test empty role name
        role.setRoleName("");
        assertEquals("", role.getRoleName(), "Should handle empty role names");

        // Test null role name
        role.setRoleName(null);
        assertNull(role.getRoleName(), "Should handle null role names");
    }

    @Test
    void testSpecialCharactersInRoleName() {
        // Test special characters
        role.setRoleName("DEV_OPS#1");
        assertEquals("DEV_OPS#1", role.getRoleName(), "Should handle special characters");

        // Test whitespace
        role.setRoleName("   space_test   ");
        assertEquals("   space_test   ", role.getRoleName(), "Should preserve whitespace");

        // Test long name
        String longName = "A".repeat(255);
        role.setRoleName(longName);
        assertEquals(longName, role.getRoleName(), "Should handle long role names");
    }

    @Test
    void testPermissionCombinations() {
        // Test all permissions enabled
        role.setCreatePermission(true);
        role.setReadPermission(true);
        role.setUpdatePermission(true);
        role.setDeletePermission(true);

        assertAllPermissionsTrue();

        // Test mixed permissions
        role.setCreatePermission(false);
        role.setReadPermission(true);
        role.setUpdatePermission(false);
        role.setDeletePermission(true);

        assertFalse(role.isCreatePermission(), "Create should be false");
        assertTrue(role.isReadPermission(), "Read should be true");
        assertFalse(role.isUpdatePermission(), "Update should be false");
        assertTrue(role.isDeletePermission(), "Delete should be true");
    }

    @Test
    void testPermissionToggle() {
        // Test toggle create permission
        role.setCreatePermission(true);
        assertTrue(role.isCreatePermission(), "Create should be true after enabling");
        role.setCreatePermission(false);
        assertFalse(role.isCreatePermission(), "Create should be false after disabling");

        // Test toggle read permission
        role.setReadPermission(true);
        assertTrue(role.isReadPermission(), "Read should be true after enabling");
        role.setReadPermission(false);
        assertFalse(role.isReadPermission(), "Read should be false after disabling");
    }

    private void assertAllPermissionsTrue() {
        assertTrue(role.isCreatePermission(), "Create permission should be true");
        assertTrue(role.isReadPermission(), "Read permission should be true");
        assertTrue(role.isUpdatePermission(), "Update permission should be true");
        assertTrue(role.isDeletePermission(), "Delete permission should be true");
    }
}