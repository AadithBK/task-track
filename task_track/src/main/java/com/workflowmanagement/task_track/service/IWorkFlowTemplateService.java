package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.dto.workflow.template.CreateTemplateDto;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.dto.workflow.template.UpdateTemplateDto;

import java.util.List;

public interface IWorkFlowTemplateService {

    //Create a new template

    public ResponseWorkflowDto createTemplate(CreateTemplateDto createTemplateDto);


    public ResponseWorkflowDto getWorkflowTemplate(Integer templateId);

    public List<ResponseWorkflowDto> getAllWorkFlowTemplates();

    public void updateWorkFlowTemplate(Integer templateId, Integer templateStepId,UpdateTemplateDto updateTemplateDto);

    public void deleteWorkFlowTemplate(Integer templateId);
}
