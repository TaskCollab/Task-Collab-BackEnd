package com.TaskCollab.dao;
<<<<<<< Updated upstream
import com.TaskCollab.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndReadFalse(Long userId);
}
=======

import com.TaskCollab.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByNotificationId(Long notificationId);
}


>>>>>>> Stashed changes
