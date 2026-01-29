package com.workflowmanagement.task_track.dto.workflow;

import com.workflowmanagement.task_track.dto.workflow.helper.HelperResponseCreateNewWorkflowDTO;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseCreateNewWorkflowDTO {
    private String workflowId;
    private Integer currentStep;

    private WorkflowStatus status;
    private String workflowName;

    private List<HelperResponseCreateNewWorkflowDTO> workflowSteps;
}
