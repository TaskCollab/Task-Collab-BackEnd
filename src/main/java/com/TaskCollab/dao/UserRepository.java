package com.TaskCollab.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TaskCollab.Entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}