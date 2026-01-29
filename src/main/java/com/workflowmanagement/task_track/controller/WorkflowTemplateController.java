package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.workflow.template.CreateTemplateDto;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.dto.workflow.template.UpdateTemplateDto;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.service.implementation.WorkflowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/workflow/template")
@PreAuthorize("hasRole('ADMIN')")
public class WorkflowTemplateController {

    @Autowired
    private WorkflowService workflowService;



    /**
     * Creates a new workflow template
     *
     * args: CreateTemplateDto
     * returns: ResponseWorkflowDto (extends createTemplateDto with workflowId field)
     *
     * Service Implimentation:
     *  Converts DTO object to WorkflowTemplate entity
     *  Reconverts the Entity to ResponseWorkflowDto, and return it
     *
     */

    @PostMapping
    @Valid
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWorkflowDto createWorkFlowTemplate(@RequestBody CreateTemplateDto reqBody){
    return workflowService.createTemplate(reqBody);
    }


    @Valid
    @GetMapping("/{templateId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseWorkflowDto getWorkFlowTemplate(@PathVariable Integer templateId) {
        return  workflowService.getWorkflowTemplate(templateId);
    }

    @GetMapping("/all")
    public List<ResponseWorkflowDto> getAllWorkflows() {
        return workflowService.getAllWorkFlowTemplates();
    }

    @PatchMapping("/{templateId}/{templateStepId}")

    public  void updateWorkflowData(@PathVariable Integer templateId, @PathVariable Integer templateStepId, @RequestBody UpdateTemplateDto updateTemplateDto) {
        workflowService.updateWorkFlowTemplate(templateId, templateStepId, updateTemplateDto);
    }

    @DeleteMapping("/{templateId}")
    public void deleteWorkFlow(@PathVariable Integer templateId) {
        workflowService.deleteWorkFlowTemplate(templateId);
    }
}
