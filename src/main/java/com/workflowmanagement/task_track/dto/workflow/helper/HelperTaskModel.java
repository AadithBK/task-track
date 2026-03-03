package com.workflowmanagement.task_track.dto.workflow.helper;

import com.workflowmanagement.task_track.enums.taskenum.TaskPriority;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Helper;

import java.time.Instant;
import java.util.List;

@NotNull
@Getter
@Setter
public class HelperTaskModel {
    private Integer taskId;
    private String title;
    private String description;
    private HelperUserModel assignedToUser;
    private HelperUserModel createdByUser;
    private List<HelperSubTaskModel> subTasks;
    private TaskPriority priority;
    private TaskStatus status;
    private Instant dueDate;
}

