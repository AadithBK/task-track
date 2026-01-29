package com.workflowmanagement.task_track.dto.workflow.template;


import com.workflowmanagement.task_track.enums.workspace.UpdateWorkflowStepOperation;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;
import lombok.Data;

import java.util.List;

@Data
public class UpdateTemplateDto {

    private  String name;
    private List<HelperUpdateTemplateDto> stepUpdate;
}
