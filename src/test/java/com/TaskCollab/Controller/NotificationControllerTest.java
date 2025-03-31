// package com.TaskCollab.Controller;

// import com.TaskCollab.Entity.Notification;
// import com.TaskCollab.dao.NotificationRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.Optional;

// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// public class NotificationControllerTest {

//     private MockMvc mockMvc;

//     @Mock
//     private NotificationRepository notificationRepository;

//     @InjectMocks
//     private NotificationController notificationController;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
//     }

//     @Test
//     public void testGetUnreadNotifications() throws Exception {
//         Notification notification = new Notification();
//         notification.setId(1L);
//         notification.setUserId(99L);
//         notification.setMessage("Task updated!");
//         notification.setRead(false);
//         notification.setTimestamp(LocalDateTime.now());

//         when(notificationRepository.findByUserIdAndReadFalse(99L)).thenReturn(Arrays.asList(notification));

//         mockMvc.perform(get("/api/notifications/user/99"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].userId").value(99L))
//                .andExpect(jsonPath("$[0].message").value("Task updated!"));
//     }

//     @Test
//     public void testMarkAsRead() throws Exception {
//         Notification notification = new Notification();
//         notification.setId(1L);
//         notification.setUserId(99L);
//         notification.setMessage("Task updated!");
//         notification.setRead(false);

//         when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

//         mockMvc.perform(post("/api/notifications/markAsRead/1")
//                         .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());

//         verify(notificationRepository, times(1)).save(notification);
//     }
// }
