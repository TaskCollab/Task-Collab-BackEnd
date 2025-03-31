package com.TaskCollab.Controller;

<<<<<<< Updated upstream
import com.TaskCollab.Entity.Notification;
import com.TaskCollab.dao.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
=======
import com.TaskCollab.dto.*;
import com.TaskCollab.Entity.NotificationInterface;
import com.TaskCollab.Entity.RoleInterface;
import com.TaskCollab.Entity.TaskInterface;
import com.TaskCollab.Service.FetchDataService;
import com.TaskCollab.Service.NotificationService;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
>>>>>>> Stashed changes

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
<<<<<<< Updated upstream
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
=======
    private NotificationService notificationService;

    //@Autowired
    //private FetchDataService fetchDataService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationByUserId(@PathVariable Long userId) {
        List<NotificationDTO> notificationByUserId = notificationService.getNotificationByUserId(userId);
        return ResponseEntity.ok(notificationByUserId);
    }

    @PutMapping("/markAsRead/{notificationId}")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long notificationId){
        
        NotificationInterface updatedNotification = notificationService.updateNotification(notificationId);
        if (updatedNotification != null) {
            return ResponseEntity.ok(convertToDTO(updatedNotification));  // Convert TaskInterface to TaskDTO
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper method to convert TaskInterface to TaskDTO
    private NotificationDTO convertToDTO(NotificationInterface notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(notification.getNotificationId());
        dto.setNotificationTitle(notification.getNotificationTitle());
        dto.setUserName(notification.getUserName());
        dto.setReadStatus(notification.isReadStatus());
        dto.setContent(notification.getContent());
        dto.setType(notification.getType());
        return dto;
    }
    
>>>>>>> Stashed changes
}
