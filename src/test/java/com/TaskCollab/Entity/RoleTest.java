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
        role.setRoleId(100);
        role.setRoleName("PROJECT_LEAD");
        role.setCreate(true);
        role.setRead(false);
        role.setUpdate(true);
        role.setDelete(false);

        assertEquals(100, role.getRoleId());
        assertEquals("PROJECT_LEAD", role.getRoleName());
        assertTrue(role.isCreate());
        assertFalse(role.isRead());
        assertTrue(role.isUpdate());
        assertFalse(role.isDelete());
    }

    @Test
    void testDefaultValues() {
        assertNull(role.getRoleId());
        assertNull(role.getRoleName());
        assertFalse(role.isCreate());
        assertFalse(role.isRead());
        assertFalse(role.isUpdate());
        assertFalse(role.isDelete());
    }

    @Test
    void testEdgeCases() {
        role.setRoleId(-1);
        role.setRoleName("");
        
        assertEquals(-1, role.getRoleId());
        assertEquals("", role.getRoleName());
    }

    @Test
    void testSpecialCharactersInRoleName() {
        role.setRoleName("DEV-OPS_SUPPORT");
        assertEquals("DEV-OPS_SUPPORT", role.getRoleName());

        role.setRoleName("DATA_ENGINEER#1");
        assertEquals("DATA_ENGINEER#1", role.getRoleName());

        role.setRoleName("   space_test   ");
        assertEquals("   space_test   ", role.getRoleName());
    }

    @Test
    void testPermissionCombinations() {
        // Test all true
        role.setCreate(true);
        role.setRead(true);
        role.setUpdate(true);
        role.setDelete(true);
        
        assertTrue(role.isCreate());
        assertTrue(role.isRead());
        assertTrue(role.isUpdate());
        assertTrue(role.isDelete());

        // Test mixed values
        role.setCreate(false);
        role.setRead(true);
        role.setUpdate(false);
        role.setDelete(true);
        
        assertFalse(role.isCreate());
        assertTrue(role.isRead());
        assertFalse(role.isUpdate());
        assertTrue(role.isDelete());
    }
}