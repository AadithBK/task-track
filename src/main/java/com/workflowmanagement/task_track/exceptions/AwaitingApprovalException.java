package com.workflowmanagement.task_track.exceptions;

public class AwaitingApprovalException extends RuntimeException {
    public AwaitingApprovalException(String message) {
        super(message);
    }
}
