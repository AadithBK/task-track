package com.workflowmanagement.task_track.controller;


import com.workflowmanagement.task_track.dto.ResponseTaskCreateDto;
import com.workflowmanagement.task_track.dto.workflow.ResponseCreateNewWorkflowDTO;
import com.workflowmanagement.task_track.dto.workflow.ResponseGetWorkflowByIDDTO;
import com.workflowmanagement.task_track.dto.workflow.WorkflowStepUpdateStatusReqDTO;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import com.workflowmanagement.task_track.model.workflow.Workflow;
import com.workflowmanagement.task_track.service.implementation.WorkflowService;
import com.workflowmanagement.task_track.util.CustomJSONResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('MANAGER')")
@RestController
@RequestMapping("/api/workflow")
@RequiredArgsConstructor
public class WorkflowController {
    @Getter
    @Setter
    public static  class CreateWorkflowReq {
        @NotNull(message = "Workflow Template ID is required")
        private Integer workflowTemplateId;

        @NotBlank(message = "Name for workflow is required")
        private String workflowName;
    }

    private final WorkflowService workflowService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomJSONResponse<ResponseCreateNewWorkflowDTO> createWorkFlowFromTemplate(@Valid @RequestBody CreateWorkflowReq reqBody){
        return  CustomJSONResponse.ok("Created new workflow template",workflowService.createWorkflowFromTemplate(reqBody.workflowTemplateId, reqBody.workflowName));
    }

@DeleteMapping("/{workflowId}")
@ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkflow(@PathVariable Integer workflowId) {
    workflowService.deleteWorkflow(workflowId);
}


@GetMapping("/{workflowId}")
    public CustomJSONResponse<ResponseGetWorkflowByIDDTO> getWorkflowById(@PathVariable Integer workflowId) {
        return  CustomJSONResponse.ok("", workflowService.getWorkflowById(workflowId));
}

    @GetMapping("/createdByMe")
    public CustomJSONResponse<List<ResponseGetWorkflowByIDDTO>> getWorkflowByUserId() {
        return  CustomJSONResponse.ok("", workflowService.getWorkflowByUserId());
    }
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/department/{department}")
    public CustomJSONResponse<List<Workflow>> getWorkflowByDepartment(@Valid @PathVariable Department department) {
    return  CustomJSONResponse.ok(null,workflowService.getWorkflowByDepartment(department));
}

//@PutMapping("/status/{workflowId}")
//    public CustomJSONResponse<?> updateStatusOfWorkfloe(@Valid @PathVariable Integer workflowId) {
//
//        return CustomJSONResponse.ok("", null);
//}


    @DeleteMapping("/{workflowId}/step/{workflowStepId}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkflowStep(@PathVariable Integer workflowId, @PathVariable Integer workflowStepId) {
        workflowService.deleteWorkflowStep(workflowId, workflowStepId);
    }

    @PatchMapping("/{workflowId}/step/{workflowStepId}/update")
    public  void updateWorkflowStep(@PathVariable Integer workflowId, @PathVariable Integer workflowStepId,@Valid @RequestBody WorkflowStepUpdateStatusReqDTO workflowStepUpdateStatusReqDTO) {
        workflowService.updateWorkflowStepStatus( workflowStepId, workflowStepUpdateStatusReqDTO);
    }
}
