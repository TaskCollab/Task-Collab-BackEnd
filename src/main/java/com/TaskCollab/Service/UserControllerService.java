package com.TaskCollab.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.TaskCollab.Entity.Users;
import com.TaskCollab.Entity.Role;
import com.TaskCollab.dao.RoleRepository;
import com.TaskCollab.dao.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserControllerService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserControllerService(UserRepository userRepository, UserService userService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    // Get user by ID
    public Users getUserById(Long id) {
        return userService.getUserById(id);
    }

    // Get all users
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    // Create a new user
    public Users createUser(Users user) {
        return userService.createUser(user);
    }

    // Update user details
    public Users updateUser(Long id, Users updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    // Delete user
    public boolean deleteUser(Long id) {
        return userService.deleteUser(id);
    }

// Assign Role to a user by Role ID
public Users updateUserRole(Long userId, String roleName) {
    Optional<Users> optionalUser = userRepository.findById(userId);
    Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);

    if (optionalUser.isPresent() && optionalRole.isPresent()) {
        Users user = optionalUser.get();
        Role role = optionalRole.get();
        user.setRole(role);
        return userRepository.save(user);
    }
    return null; // or throw an exception indicating user or role not found
}
}
