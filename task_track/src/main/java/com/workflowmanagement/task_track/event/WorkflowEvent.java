package com.workflowmanagement.task_track.event;

import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;

public class WorkflowEvent {
    private final Integer userId;
    private final NotificationType type;
    private final String message;

    public WorkflowEvent(Integer userId, NotificationType type, String message) {
        this.userId = userId;
        this.type = type;
        this.message = message;
    }
    public Integer getUserId() { return userId; }
    public NotificationType getType() { return type; }
    public String getMessage() { return message; }
}