package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import com.workflowmanagement.task_track.model.Notification;

import java.util.List;

public interface INotificationService {
    public Notification createNotification(Integer userId, NotificationType type, String message);
    public List<Notification> getNotifications(Long userId);
}
