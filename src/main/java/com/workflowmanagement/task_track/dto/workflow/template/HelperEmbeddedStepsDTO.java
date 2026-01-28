package com.workflowmanagement.task_track.dto.workflow.template;

import jakarta.validation.constraints.NotNull;
import lombok.Data;



@NotNull
@Data
public class HelperEmbeddedStepsDTO {

    private String workflowStepName;
    private int workflowStepPosition;
}
