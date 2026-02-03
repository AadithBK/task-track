package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.dto.workflow.ResponseCreateNewWorkflowDTO;
import com.workflowmanagement.task_track.dto.workflow.ResponseGetWorkflowByIDDTO;
import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStatus;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import com.workflowmanagement.task_track.mapper.WorkflowTemplateMapper;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.model.workflow.Workflow;
import com.workflowmanagement.task_track.model.workflow.WorkflowStep;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.repository.UserRepository;
import com.workflowmanagement.task_track.repository.WorkflowRepository;
import com.workflowmanagement.task_track.repository.WorkflowStepsRepository;
import com.workflowmanagement.task_track.repository.WorkflowTemplateRepository;
import com.workflowmanagement.task_track.service.IWorkflowService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowService implements IWorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowTemplateRepository workflowTemplateRepository;
    private final WorkflowTemplateMapper workflowTemplateMapper;
    private final UserRepository userRepository;
    private final WorkflowStepsRepository workflowStepsRepository;


    @Override
    public ResponseCreateNewWorkflowDTO createWorkflowFromTemplate(Integer workflowTemplateID) {
        WorkflowTemplate workflowTemplate = workflowTemplateRepository.findById(workflowTemplateID).orElseThrow(() -> new EntityNotFoundException("Workflow Template with ID " + workflowTemplateID + " does not exist"));

        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        Integer user = principal.user().getUserId();

        User managedUser = userRepository.findById(principal.user().getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        Workflow createWorkFlowFromTemplate = Workflow
                .builder()
//                .workflowSteps(createWorkFlowSteps)
                .workflowName(workflowTemplate.getWorkflowTemplateName())
//                .currentStep(createWorkFlowSteps.getFirst().getWorkflowStepId())
                .status(WorkflowStatus.PENDING)
                .createdBy(managedUser)
                .build();
        List<WorkflowStep> createWorkFlowSteps =  workflowTemplate
                .getWorkflowSteps()
                .stream()
                .map(workflowTemplateStep -> WorkflowStep
                        .builder()
                        .stepName(workflowTemplateStep.getWorkflowTemplateStepName())
                        .step(workflowTemplateStep.getWorkflowTemplateStepPosition())
                        .status(WorkflowStepStatus.PENDING)
                        .workflow(createWorkFlowFromTemplate)
                        .build()
                ).toList();

        createWorkFlowFromTemplate.setWorkflowSteps(createWorkFlowSteps);
        createWorkFlowFromTemplate.setCurrentStep(createWorkFlowSteps.getFirst().getWorkflowStepId());

        return workflowTemplateMapper.toCustomWorkflowResponse( workflowRepository.save(createWorkFlowFromTemplate));
    }

    @Override
    public void deleteWorkflow(Integer workflowId) {
        workflowRepository.deleteById(workflowId);
    }

    @Override
    public ResponseGetWorkflowByIDDTO getWorkflowById(Integer workflowId) {
        Workflow workflow = workflowRepository.findById(workflowId).orElseThrow(() ->  new EntityNotFoundException("Workflow with ID " + workflowId + " not found"));
        List<WorkflowStep> orderedSteps = workflowStepsRepository.findByWorkflow_WorkflowIdOrderByStepAsc(workflowId);

        // 3. Attach the ordered list back to the workflow object (optional, for the return DTO)
        workflow.setWorkflowSteps(orderedSteps);
        return workflowTemplateMapper.toGetByIdDto(workflow);
    }

    @Override
    public List<Workflow> getWorkflowByDepartment(Department department) {
//        List<Workflow> workflow = workflowRepository.find
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        Integer user = principal.user().getUserId();

        System.out.println("User ID to bind: " + principal.user().getUserId());;
        List<Workflow> workflows = workflowRepository.findByCreatedBy_Department(department).orElseThrow(() ->  new EntityNotFoundException("No workflow created for Department " + department));
        return workflows;
    }


}
