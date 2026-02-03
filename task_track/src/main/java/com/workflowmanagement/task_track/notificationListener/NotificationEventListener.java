package com.workflowmanagement.task_track.notificationListener;

import com.workflowmanagement.task_track.event.WorkflowEvent;
import com.workflowmanagement.task_track.service.implementation.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private final NotificationService service;

    public NotificationEventListener(NotificationService service) {
        this.service = service;
    }

    @EventListener
    public void onWorkflowEvent(WorkflowEvent event) {
        service.createNotification(event.getUserId(), event.getType(), event.getMessage());
    }
}