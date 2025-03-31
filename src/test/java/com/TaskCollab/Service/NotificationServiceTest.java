// package com.TaskCollab.Service;

// import com.TaskCollab.Entity.Notification;
// import com.TaskCollab.dao.NotificationRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import java.time.LocalDateTime;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// public class NotificationServiceTest {

//     @Mock
//     private NotificationRepository notificationRepository;

//     @InjectMocks
//     private NotificationService notificationService;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     public void testSendNotification_CreatesAndSavesNotification() {
//         Long userId = 1L;
//         String message = "Task Updated!";

//         // Call the method
//         notificationService.sendNotification(userId, message);

//         // Verify that a notification is saved with correct values
//         verify(notificationRepository, times(1)).save(argThat(notification ->
//             notification.getUserId().equals(userId) &&
//             notification.getMessage().equals(message) &&
//             !notification.isRead() &&
//             notification.getTimestamp() != null
//         ));
//     }
// }
