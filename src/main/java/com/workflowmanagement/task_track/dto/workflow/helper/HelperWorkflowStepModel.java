package com.workflowmanagement.task_track.dto.workflow.helper;

import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class HelperWorkflowStepModel {
    private Integer workflowStepId;
    private String stepName;
    private WorkflowStepStatus status;
    private List<HelperTaskModel> task;
}



