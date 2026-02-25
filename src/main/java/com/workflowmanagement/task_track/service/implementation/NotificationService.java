package com.workflowmanagement.task_track.service.implementation;


import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import com.workflowmanagement.task_track.model.Notification;
import com.workflowmanagement.task_track.repository.NotificationRepository;
import com.workflowmanagement.task_track.service.INotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class NotificationService implements INotificationService {

    private final NotificationRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    public Notification createNotification(Integer userId, NotificationType type, String message) {

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setMessage(message);

        Notification saved = repository.save(notification);

        // PUSH REALTIME EVENT
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + userId,
                saved
        );

        return saved;
    }

    public List<Notification> getNotifications(Long userId) {
        return repository.findByUserId(userId);
    }

    public Notification markAsRead(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setStatus("READ");
        return repository.save(notification);
    }

    public long getUnreadCount(Long userId) {
        return repository.countByUserIdAndStatus(userId, "UNREAD");
    }
}