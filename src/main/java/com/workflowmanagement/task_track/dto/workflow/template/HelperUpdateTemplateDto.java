package com.workflowmanagement.task_track.dto.workflow.template;

import com.workflowmanagement.task_track.enums.workspace.UpdateWorkflowStepOperation;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;
import lombok.Data;
@Data
public class HelperUpdateTemplateDto {
    private UpdateWorkflowStepOperation operation;
    private int position;
    private String name;
    private HelperEmbeddedStepsDTO newStep;
}
