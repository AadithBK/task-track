package com.workflowmanagement.task_track.dto.workflow.helper;

import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import lombok.Data;

@Data
public class HelperResponseCreateNewWorkflowDTO {
    private Integer workflowStepId;
    private WorkflowStepStatus status;
    private String stepName;
}
