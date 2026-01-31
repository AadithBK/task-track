package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.workflow.template.CreateTemplateDto;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.dto.workflow.template.UpdateTemplateDto;
import com.workflowmanagement.task_track.service.implementation.WorkflowTemplateService;
import com.workflowmanagement.task_track.util.CustomJSONResponse;
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
    private WorkflowTemplateService workflowTemplateService;



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

    @ResponseStatus(HttpStatus.CREATED)
    public CustomJSONResponse<ResponseWorkflowDto> createWorkFlowTemplate(@Valid @RequestBody CreateTemplateDto reqBody){
    return CustomJSONResponse.ok("New Template created",workflowTemplateService.createTemplate(reqBody));
    }


    @Valid
    @GetMapping("/{templateId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomJSONResponse<ResponseWorkflowDto> getWorkFlowTemplate(@Valid @PathVariable Integer templateId) {
        return  CustomJSONResponse.ok(null,workflowTemplateService.getWorkflowTemplate(templateId));
    }

    @GetMapping("/all")
    public CustomJSONResponse<List<ResponseWorkflowDto>> getAllWorkflows() {
        return CustomJSONResponse.ok(null,workflowTemplateService.getAllWorkFlowTemplates());
    }

    @PatchMapping("/{templateId}/{templateStepId}")
@ResponseStatus(HttpStatus.CREATED)
    public  void updateWorkflowData(@PathVariable Integer templateId, @PathVariable Integer templateStepId, @RequestBody UpdateTemplateDto updateTemplateDto) {
        workflowTemplateService.updateWorkFlowTemplate(templateId, templateStepId, updateTemplateDto);
    }

    @DeleteMapping("/{templateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkFlow(@PathVariable Integer templateId) {
        workflowTemplateService.deleteWorkFlowTemplate(templateId);
    }
}
