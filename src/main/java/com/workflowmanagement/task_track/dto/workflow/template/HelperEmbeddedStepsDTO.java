package com.workflowmanagement.task_track.dto.workflow.template;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@NotNull
@Data
public class HelperEmbeddedStepsDTO {
@Valid
    private String workflowStepName;
@Valid
private Integer workflowStepPosition;
}
