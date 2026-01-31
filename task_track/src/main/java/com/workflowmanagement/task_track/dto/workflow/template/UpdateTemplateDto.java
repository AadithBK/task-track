package com.workflowmanagement.task_track.dto.workflow.template;


import lombok.Data;

import java.util.List;

@Data
public class UpdateTemplateDto {

    private  String name;
    private List<HelperUpdateTemplateDto> stepUpdate;
}
