package com.workflowmanagement.task_track.dto;

import com.workflowmanagement.task_track.dto.workflow.helper.HelperSubTaskModel;
import com.workflowmanagement.task_track.enums.taskenum.TaskPriority;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import com.workflowmanagement.task_track.model.SubTask;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class TaskDto {

    @NotNull
    private Integer taskId;

    @NotNull
    private String title;

    private String description;

    @NotNull
    private  Integer assignedToUser;

    @NotNull
    private Integer createdByUser;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    private Instant dueDate;

    private List<HelperSubTaskModel> subTasks;
//

}
