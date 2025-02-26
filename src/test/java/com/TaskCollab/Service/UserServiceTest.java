package com.TaskCollab.Service;

import com.TaskCollab.Entity.Role;
import com.TaskCollab.Entity.Users;
import com.TaskCollab.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void loadUserByUsername_ValidUser_ReturnsCorrectUserDetails() {
        // Arrange
        String username = "testUser";
        Users user = createTestUser(username, createAdminRole());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo("password");
        assertAuthoritiesContainExactly(userDetails, "ROLE_ADMIN", "CREATE", "READ");
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        // Arrange
        String username = "unknownUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, 
            () -> userService.loadUserByUsername(username));
    }

    @Test
    void loadUserByUsername_FullPermissions_ReturnsAllAuthorities() {
        // Arrange
        String username = "superUser";
        Users user = createTestUser(username, createFullPermissionsRole());
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertAuthoritiesContainExactly(userDetails, 
            "ROLE_SUPERUSER", "CREATE", "READ", "UPDATE", "DELETE");
    }

    @Test
    void loadUserByUsername_NoPermissions_ReturnsOnlyRole() {
        // Arrange
        String username = "observer";
        Users user = createTestUser(username, createNoPermissionsRole());
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertAuthoritiesContainExactly(userDetails, "ROLE_OBSERVER");
    }

    // Helper methods
    private Users createTestUser(String username, Role role) {
        Users user = new Users();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(role);
        return user;
    }

    private Role createAdminRole() {
        Role role = new Role();
        role.setRoleName("ADMIN");
        role.setCreatePermission(true);
        role.setReadPermission(true);
        role.setUpdatePermission(false);
        role.setDeletePermission(false);
        return role;
    }

    private Role createFullPermissionsRole() {
        Role role = new Role();
        role.setRoleName("SUPERUSER");
        role.setCreatePermission(true);
        role.setReadPermission(true);
        role.setUpdatePermission(true);
        role.setDeletePermission(true);
        return role;
    }

    private Role createNoPermissionsRole() {
        Role role = new Role();
        role.setRoleName("OBSERVER");
        role.setCreatePermission(false);
        role.setReadPermission(false);
        role.setUpdatePermission(false);
        role.setDeletePermission(false);
        return role;
    }

    private void assertAuthoritiesContainExactly(UserDetails userDetails, String... expected) {
        Set<String> authorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
        
        assertThat(authorities).containsExactlyInAnyOrder(expected);
    }
}