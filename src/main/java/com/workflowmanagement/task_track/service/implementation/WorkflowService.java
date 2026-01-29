package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.dto.workflow.ResponseCreateNewWorkflowDTO;
import com.workflowmanagement.task_track.dto.workflow.ResponseGetWorkflowByIDDTO;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStatus;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import com.workflowmanagement.task_track.mapper.WorkflowTemplateMapper;
import com.workflowmanagement.task_track.model.workflow.Workflow;
import com.workflowmanagement.task_track.model.workflow.WorkflowStep;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.repository.WorkflowRepository;
import com.workflowmanagement.task_track.repository.WorkflowTemplateRepository;
import com.workflowmanagement.task_track.service.IWorkflowService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowService implements IWorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowTemplateRepository workflowTemplateRepository;
    private final WorkflowTemplateMapper workflowTemplateMapper;
    @Override
    public ResponseCreateNewWorkflowDTO createWorkflowFromTemplate(Integer workflowTemplateID) {
        WorkflowTemplate workflowTemplate = workflowTemplateRepository.findById(workflowTemplateID).orElseThrow(() -> new EntityNotFoundException("Workflow Template with ID " + workflowTemplateID + " does not exist"));
        List<WorkflowStep> createWorkFlowSteps =  workflowTemplate
                .getWorkflowSteps()
                .stream()
                .map(workflowTemplateStep -> WorkflowStep
                        .builder()
                        .stepName(workflowTemplateStep.getWorkflowTemplateStepName())
                        .status(WorkflowStepStatus.PENDING)
                        .build()
                ).toList();

        Workflow createWorkFlowFromTemplate = Workflow
                .builder()
                .workflowSteps(createWorkFlowSteps)
                .workflowName(workflowTemplate.getWorkflowTemplateName())
                .currentStep(createWorkFlowSteps.getFirst().getWorkflowStepId())
                .status(WorkflowStatus.PENDING)
                .build();


        return workflowTemplateMapper.toCustomWorkflowResponse( workflowRepository.save(createWorkFlowFromTemplate));
    }

    @Override
    public void deleteWorkflow(Integer workflowId) {
        workflowRepository.deleteById(workflowId);
    }

    @Override
    public ResponseGetWorkflowByIDDTO getWorkflowById(Integer workflowId) {
        Workflow workflow = workflowRepository.findById(workflowId).orElseThrow(() ->  new EntityNotFoundException("Workflow with ID " + workflowId + " not found"));

        return workflowTemplateMapper.toGetByIdDto(workflow);
    }
}
