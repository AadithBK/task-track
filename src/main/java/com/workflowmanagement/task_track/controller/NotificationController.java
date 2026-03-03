package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.model.Notification;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.service.implementation.NotificationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
//@CrossOrigin(origins = "http://localhost:4200")
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

    @GetMapping("/")
    public List<Notification> getUserNotifications() {
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        var principal = (UserPrincipal) auth.getPrincipal();
        assert principal != null;
        Integer user = principal.user().getUserId();
        return service.getNotifications(user);
    }

    @PatchMapping("/{id}/read")
    public Notification markAsRead(@PathVariable Integer id) {

        return service.markAsRead(id);
    }

    @GetMapping("/unread-count")
    public long getUnreadCount() {
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        var principal = (UserPrincipal) auth.getPrincipal();
        assert principal != null;
        Integer user = principal.user().getUserId();
        return service.getUnreadCount(user);
    }
}