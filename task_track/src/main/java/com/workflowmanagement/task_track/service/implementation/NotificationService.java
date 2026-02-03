package com.workflowmanagement.task_track.service.implementation;


import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import com.workflowmanagement.task_track.model.Notification;
import com.workflowmanagement.task_track.repository.NotificationRepository;
import com.workflowmanagement.task_track.service.INotificationService;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class NotificationService implements INotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public Notification createNotification(Integer userId, NotificationType type, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setMessage(message);
        return repository.save(notification);
    }

    public List<Notification> getNotifications(Long userId) {
        return repository.findByUserId(userId);
    }
}