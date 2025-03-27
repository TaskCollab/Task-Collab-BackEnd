package com.TaskCollab.Controller;

import com.TaskCollab.Entity.Notification;
import com.TaskCollab.dao.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/user/{userId}")
    public List<Notification> getUnreadNotifications(@PathVariable Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }

    @PostMapping("/markAsRead/{id}")
    public void markAsRead(@PathVariable Long id) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}
