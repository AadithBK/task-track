package com.workflowmanagement.task_track.dto.user;

import com.workflowmanagement.task_track.enums.Role;
import com.workflowmanagement.task_track.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserActivationDto {
    @NotNull
    private Role role;
    @NotNull
    private Status status;
}