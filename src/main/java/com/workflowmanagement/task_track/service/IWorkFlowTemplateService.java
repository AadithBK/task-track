package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.dto.workflow.template.CreateTemplateDto;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;

import java.util.List;

public interface IWorkFlowTemplateService {

    public ResponseWorkflowDto createTemplate(CreateTemplateDto createTemplateDto);

}
