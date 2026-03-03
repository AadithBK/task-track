package com.workflowmanagement.task_track.dto;

import com.workflowmanagement.task_track.enums.taskenum.TaskPriority;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import com.workflowmanagement.task_track.model.User;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Data
public class TaskCreateDto {

    @NotBlank
    private String title;

    private String description;

    private Integer assignedToUserId;

    private TaskPriority priority;
    private TaskStatus status;
    private Instant dueDate;

    private Integer workflowStepId;

}
