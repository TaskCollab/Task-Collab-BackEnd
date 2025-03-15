
package com.TaskCollab.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.TaskCollab.Entity.Role;
import com.TaskCollab.Entity.Users;

public class UserTest {

    private Users user;
    private Role role;

    @BeforeEach
    void setUp() {
        user = new Users();
        role = new Role();
        role.setRoleId(1);
    }

    @Test
    void testSetAndGetUserId() {
        user.setUserId(1L);
        assertEquals(1L, user.getUserId());
    }

    @Test
    void testSetAndGetUsername() {
        user.setUsername("testUser");
        assertEquals("testUser", user.getUsername());
    }

    @Test
    void testSetAndPassword() {
        user.setPassword("password");
        assertEquals("password", user.getPassword());
    }

    @Test
    void testSetAndGetRole() {
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    void testSetAndGetIsAdmin() {
        user.setIsAdmin(true);
        assertTrue(user.getIsAdmin());
    }
}
