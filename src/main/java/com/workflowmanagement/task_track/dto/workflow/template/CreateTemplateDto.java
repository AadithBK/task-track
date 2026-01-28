package com.workflowmanagement.task_track.dto.workflow.template;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@NotNull
@Data
public class CreateTemplateDto {

    private String workflowTemplateName;

    @Size(min = 1, message = "Number of steps less than 1")
    private List<HelperEmbeddedStepsDTO> workflowSteps;

}


