package com.TaskCollab.Service;
<<<<<<< Updated upstream
import com.TaskCollab.Entity.Notification;
import com.TaskCollab.dao.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(Long userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
    }
=======

import java.util.List;
import com.TaskCollab.dto.NotificationDTO;
import com.TaskCollab.dto.TaskDTO;
import com.TaskCollab.Entity.Notification;
import com.TaskCollab.Entity.NotificationInterface;
import com.TaskCollab.Entity.Task;
import com.TaskCollab.Entity.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class NotificationService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<NotificationDTO> getNotificationByUserId(Long userId) {

        // Create CriteriaBuilder.
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        // Create Root of notification and user, then inner join.
        Root<Notification> notificationRoot = cq.from(Notification.class);
        Join<Notification, Users> userJoin = notificationRoot.join("user", JoinType.INNER);

        // Select fields
        cq.multiselect(
            notificationRoot.get("notificationId").alias("notificationId"),  // Match DTO
            userJoin.get("username").alias("userName"),                       // username → userName
            notificationRoot.get("content").alias("content"),
            notificationRoot.get("type").alias("type"),
            notificationRoot.get("readStatus").alias("readStatus"),
            notificationRoot.get("notificationTitle").alias("notificationTitle")
        ).where(cb.equal(userJoin.get("userId"), userId));  // Filter by userId


        List<Tuple> result = entityManager.createQuery(cq).getResultList();

        return result.stream().map(tuple -> {
            NotificationDTO dto = new NotificationDTO();
            
            dto.setNotificationId(tuple.get("notificationId", Long.class));
            dto.setUserName(tuple.get("userName", String.class)); // Corrected here
            dto.setContent(tuple.get("content", String.class));
            dto.setType(tuple.get("type", String.class));
            dto.setReadStatus(tuple.get("readStatus", Boolean.class));
            dto.setNotificationTitle(tuple.get("notificationTitle", String.class));
            return dto;
        }).collect(Collectors.toList());
    }
    @Transactional
    public NotificationInterface updateNotification(Long notificationId) {
        boolean readStatus = true;
        
        // Create CriteriaBuilder
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Notification> update = cb.createCriteriaUpdate(Notification.class);

        // Create Root of Notification.
        Root<Notification> root = update.from(Notification.class);

        //Update the read status.
        update.set("readStatus",readStatus);
        
        // Apply condition. Explicitly specify the type of 'id' (Long) in where condition
        update.where(cb.equal(root.get("id").as(Long.class), notificationId));

        // Execute update
        int updatedRows = entityManager.createQuery(update).executeUpdate();
        
        // Fix: Check 'updatedRows' value properly
        if (updatedRows > 0) {
            return entityManager.find(Notification.class, notificationId);
        }
        
        return null;  // If no rows were updated, return null
    }

    // Try to create a notification and print out the prompt line to show whether the code runs
    // successfully.
    /*@Transactional
    public void createNotification(Task task, String title) {
        // Get user from same transaction context
        Users user = entityManager.createQuery(
                "SELECT u FROM Users u WHERE u.username = :username", 
                Users.class)
            .setParameter("username", task.getAssigned_To())
            .getSingleResult();

        Notification notification = new Notification();
        notification.setContent(task.getDescription());
        notification.setType("Task");
        notification.setReadStatus(false);
        notification.setNotificationTitle(title);
        
        entityManager.persist(notification);
    }*/

       
>>>>>>> Stashed changes
}
