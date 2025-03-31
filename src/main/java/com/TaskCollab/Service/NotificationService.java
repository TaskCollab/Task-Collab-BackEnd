package com.TaskCollab.Service;
import com.TaskCollab.Entity.Notification;
import com.TaskCollab.dao.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    
    private final NotificationRepository notificationRepository;

    @Autowired

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    public void sendNotification(Long userId, String message) {
        Notification notification = buildNotification(userId, message);
        notificationRepository.save(notification);
    }

    private Notification buildNotification(Long userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setTimestamp(LocalDateTime.now());
        return notification;
    }
}
