package com.TaskCollab.Service;

import com.TaskCollab.Entity.Role;
import com.TaskCollab.Entity.Users;
import com.TaskCollab.dao.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserControllerService userControllerService;

    private Role testRole;
    private Users testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testRole = new Role();
        testRole.setRoleId(1);
        testRole.setRoleName("USER");

        testUser = new Users();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(testRole);
        testUser.setIsAdmin(false);
    }

    @Test
    public void testCreateUser() {
        when(userService.createUser(any(Users.class))).thenReturn(testUser);

        Users created = userControllerService.createUser(testUser);

        assertNotNull(created);
        assertEquals("testuser", created.getUsername());
        verify(userService, times(1)).createUser(testUser);
    }

    @Test
    public void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(testUser);

        Users found = userControllerService.getUserById(1L);

        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(testUser));

        List<Users> users = userControllerService.getAllUsers();

        assertEquals(1, users.size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testUpdateUser() {
        when(userService.updateUser(eq(1L), any(Users.class))).thenReturn(testUser);

        Users updated = userControllerService.updateUser(1L, testUser);

        assertNotNull(updated);
        assertEquals("testuser", updated.getUsername());
        verify(userService).updateUser(1L, testUser);
    }

    @Test
    public void testDeleteUser() {
        when(userService.deleteUser(1L)).thenReturn(true);

        boolean deleted = userControllerService.deleteUser(1L);

        assertTrue(deleted);
        verify(userService).deleteUser(1L);
    }

    @Test
    public void testUpdateUserRole_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(Users.class))).thenReturn(testUser);

        Users result = userControllerService.updateUserRole(1L, testRole);

        assertNotNull(result);
        assertEquals("USER", result.getRole().getRoleName());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(Users.class));
    }

    @Test
    public void testUpdateUserRole_userNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Users result = userControllerService.updateUserRole(999L, testRole);

        assertNull(result);
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testUpdateUserRole_nullRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Users result = userControllerService.updateUserRole(1L, null);

        assertNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }
}
