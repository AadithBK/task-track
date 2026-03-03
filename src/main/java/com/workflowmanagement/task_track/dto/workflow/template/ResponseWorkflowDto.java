package com.workflowmanagement.task_track.dto.workflow.template;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NotNull
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWorkflowDto extends  CreateTemplateDto{
    private int workflowTemplateId;
    private String createdBy;
    private String createdAt;




}
