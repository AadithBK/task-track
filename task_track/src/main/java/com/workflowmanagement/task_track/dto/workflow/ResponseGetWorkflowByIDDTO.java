package com.workflowmanagement.task_track.dto.workflow;

import com.workflowmanagement.task_track.dto.workflow.helper.HelperWorkflowStepModel;
import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStatus;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseGetWorkflowByIDDTO {
    private Integer workflowId;
    private String workflowName;
    private Integer currentStep;
    private WorkflowStatus status;
    private List<HelperWorkflowStepModel> workflowSteps;

}
