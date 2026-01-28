package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.workflow.template.CreateTemplateDto;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.service.implementation.WorkflowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workflow/template")
@PreAuthorize("hasRole('ADMIN')")
public class WorkflowTemplateController {

    @Autowired
    private WorkflowService workflowService;
    @PostMapping
    @Valid
    public ResponseWorkflowDto createWorkFlowTemplate(@RequestBody CreateTemplateDto reqBody){
    return workflowService.createTemplate(reqBody);
    }
}
