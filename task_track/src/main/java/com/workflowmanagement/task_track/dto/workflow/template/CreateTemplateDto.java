package com.workflowmanagement.task_track.dto.workflow.template;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@NotNull
@Data

public class CreateTemplateDto {

//    private int workflowTemplateId;
    @Valid
    private String workflowTemplateName;

    @NotNull(message = "Steps list is required")
    @Size(min = 1, message = "Number of steps less than 1")
    private List<HelperEmbeddedStepsDTO> workflowSteps;


}


