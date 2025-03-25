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

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private Users testUser;
    private Role testRole;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testRole = new Role();
        testRole.setRoleId(1);
        testRole.setRoleName("USER");
        testRole.setCreatePermission(true);
        testRole.setReadPermission(true);
        testRole.setUpdatePermission(true);
        testRole.setDeletePermission(true);

        testUser = new Users();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(testRole);
        testUser.setIsAdmin(false);
    }

    @Test
    public void testLoadUserByUsername_found() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        var userDetails = userService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    public void testLoadUserByUsername_notFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.loadUserByUsername("ghost");
        });
    }

    @Test
    public void testGetUserById_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Users result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void testGetUserById_notFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Users result = userService.getUserById(999L);

        assertNull(result);
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<Users> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(Users.class))).thenReturn(testUser);

        Users newUser = new Users();
        newUser.setUsername("createdUser");
        newUser.setPassword("newpass");

        Users saved = userService.createUser(newUser);

        assertNotNull(saved);
        verify(userRepository).save(newUser);
    }

    @Test
    public void testUpdateUser_found() {
        Users updatedData = new Users();
        updatedData.setUsername("updatedUser");
        updatedData.setPassword("newpass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(Users.class))).thenReturn(testUser);

        Users result = userService.updateUser(1L, updatedData);

        assertNotNull(result);
        verify(userRepository).save(testUser);
    }

    @Test
    public void testUpdateUser_notFound() {
        Users updatedData = new Users();
        updatedData.setUsername("updatedUser");
        updatedData.setPassword("newpass");

        when(userRepository.findById(123L)).thenReturn(Optional.empty());

        Users result = userService.updateUser(123L, updatedData);

        assertNull(result);
    }

    @Test
    public void testDeleteUser_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean deleted = userService.deleteUser(1L);

        assertTrue(deleted);
        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testDeleteUser_notFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        boolean deleted = userService.deleteUser(999L);

        assertFalse(deleted);
        verify(userRepository, never()).deleteById(anyLong());
    }
}
