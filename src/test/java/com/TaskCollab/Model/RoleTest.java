
package com.TaskCollab.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.TaskCollab.Entity.Role;

public class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void testSetAndGetRoleId() {
        role.setRoleId(1);
        assertEquals(1, role.getRoleId());
    }

    @Test
    void testSetAndGetRoleName() {
        role.setRoleName("Admin");
        assertEquals("Admin", role.getRoleName());
    }

    @Test
    void testSetAndIsCreatePermission() {
        role.setCreatePermission(true);
        assertTrue(role.isCreatePermission());
    }

    @Test
    void testSetAndIsReadPermission() {
        role.setReadPermission(true);
        assertTrue(role.isReadPermission());
    }

    @Test
    void testSetAndDeletePermission() {
        role.setDeletePermission(true);
        assertTrue(role.isDeletePermission());
    }

    @Test
    void testSetAndUpdatePermission() {
        role.setUpdatePermission(true);
        assertTrue(role.isUpdatePermission());
    }
}
