package com.TaskCollab.Service;

import com.TaskCollab.Entity.Role;
import com.TaskCollab.Entity.User;
import com.TaskCollab.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
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
    void loadUserByUsername_UserExists_ReturnsCorrectUserDetails() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        Role role = new Role();
        role.setRoleName("ADMIN");
        role.setCreate(true);
        role.setRead(true);
        role.setUpdate(false);
        role.setDelete(false);
        user.setRoles(Set.of(role));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo("password");

        Set<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(authorities).containsExactlyInAnyOrder("ROLE_ADMIN", "CREATE", "READ");
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(username));
        assertThat(exception.getMessage()).isEqualTo("User not found!");
    }

    @Test
    void loadUserByUsername_UserWithMultipleRoles_CombinesAuthorities() {
        String username = "multiRoleUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        adminRole.setCreate(true);
        adminRole.setRead(true);

        Role userRole = new Role();
        userRole.setRoleName("USER");
        userRole.setUpdate(true);
        userRole.setDelete(true);

        user.setRoles(Set.of(adminRole, userRole));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        Set<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(authorities).containsExactlyInAnyOrder(
                "ROLE_ADMIN", "CREATE", "READ",
                "ROLE_USER", "UPDATE", "DELETE"
        );
    }

    @Test
    void loadUserByUsername_RoleWithNoPermissions_OnlyRoleAuthorityAdded() {
        String username = "noPermUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        Role role = new Role();
        role.setRoleName("OBSERVER");
        role.setCreate(false);
        role.setRead(false);
        role.setUpdate(false);
        role.setDelete(false);
        user.setRoles(Set.of(role));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        Set<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(authorities).containsExactly("ROLE_OBSERVER");
    }

    @Test
    void loadUserByUsername_RoleWithAllPermissions_AllAuthoritiesAdded() {
        String username = "allPermUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        Role role = new Role();
        role.setRoleName("SUPERUSER");
        role.setCreate(true);
        role.setRead(true);
        role.setUpdate(true);
        role.setDelete(true);
        user.setRoles(Set.of(role));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        Set<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(authorities).containsExactlyInAnyOrder(
                "ROLE_SUPERUSER", "CREATE", "READ", "UPDATE", "DELETE"
        );
    }

    @Test
    void loadUserByUsername_UserWithNoRoles_AuthoritiesEmpty() {
        String username = "noRolesUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRoles(Collections.emptySet()); // Explicit empty roles

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertThat(userDetails.getAuthorities()).isEmpty();
    }
}