package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.model.Notification;
import com.workflowmanagement.task_track.service.implementation.NotificationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // @PostMapping
//    public Notification createNotification(
//            @RequestParam Integer userId,
//            @RequestParam NotificationType type,
//            @RequestParam String message) {
//        return service.createNotification(userId, type, message);
//    }

    @GetMapping("/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Long userId) {
        return service.getNotifications(userId);
    }

    @PatchMapping("/{id}/read")
    public Notification markAsRead(@PathVariable Long id) {
        return service.markAsRead(id);
    }

    @GetMapping("/{userId}/unread-count")
    public long getUnreadCount(@PathVariable Long userId) {
        return service.getUnreadCount(userId);
    }
}