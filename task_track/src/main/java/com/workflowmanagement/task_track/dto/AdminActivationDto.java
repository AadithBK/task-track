package com.workflowmanagement.task_track.dto;

import com.workflowmanagement.task_track.enums.Role;
import com.workflowmanagement.task_track.enums.Status;
import lombok.Data;

@Data
public class AdminActivationDto {
    private Role role;
    private Status status;
}