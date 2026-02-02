package com.workflowmanagement.task_track.dto.workflow.template;

import com.workflowmanagement.task_track.enums.workspace.UpdateWorkflowStepOperation;
import lombok.Data;
@Data
public class HelperUpdateTemplateDto {
    private UpdateWorkflowStepOperation operation;
    private int position;
    private String name;
    private HelperEmbeddedStepsDTO newStep;
}
