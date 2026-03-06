package com.workflowmanagement.task_track;

import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import com.workflowmanagement.task_track.model.Notification;
import com.workflowmanagement.task_track.repository.NotificationRepository;
import com.workflowmanagement.task_track.service.implementation.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void createNotification_ShouldSaveAndReturnNotification() {
        // Arrange
        Integer userId = 101;
        NotificationType type = NotificationType.TASK_ASSIGNED; // Adjust based on your actual Enum values
        String message = "A new task has been assigned to you.";

        Notification savedNotification = new Notification();
        savedNotification.setUserId(1); // Simulating generated DB ID
        savedNotification.setUserId(userId);
        savedNotification.setType(type);
        savedNotification.setMessage(message);

        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        // Act
        Notification result = notificationService.createNotification(userId, type, message);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(message, result.getMessage());
        assertEquals(type, result.getType());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void getNotifications_ShouldReturnListOfNotifications() {
        // Arrange
        Long userId = 101L; // Note: Service uses Long here
        Notification n1 = new Notification();
        n1.setMessage("Message 1");
        Notification n2 = new Notification();
        n2.setMessage("Message 2");

        when(notificationRepository.findByUserId(userId)).thenReturn(List.of(n1, n2));

        // Act
        List<Notification> result = notificationService.getNotifications(userId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Message 1", result.get(0).getMessage());
        verify(notificationRepository, times(1)).findByUserId(userId);
    }
}