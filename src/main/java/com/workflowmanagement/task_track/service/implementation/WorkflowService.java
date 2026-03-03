package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.dto.workflow.ResponseCreateNewWorkflowDTO;
import com.workflowmanagement.task_track.dto.workflow.ResponseGetWorkflowByIDDTO;
import com.workflowmanagement.task_track.dto.workflow.WorkflowStepUpdateStatusReqDTO;
import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import com.workflowmanagement.task_track.enums.subTaskEnum.SubTaskStatusEnum;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStatus;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import com.workflowmanagement.task_track.event.WorkflowEvent;
import com.workflowmanagement.task_track.exceptions.WorkflowUpdateException;
import com.workflowmanagement.task_track.mapper.WorkflowTemplateMapper;
import com.workflowmanagement.task_track.model.Comment;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.model.workflow.Workflow;
import com.workflowmanagement.task_track.model.workflow.WorkflowStep;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.repository.*;
import com.workflowmanagement.task_track.service.IWorkflowService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jdbc.Work;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.ConnectionBuilder;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowService implements IWorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowTemplateRepository workflowTemplateRepository;
    private final WorkflowTemplateMapper workflowTemplateMapper;
    private final UserRepository userRepository;
    private final WorkflowStepsRepository workflowStepsRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;
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

        Workflow savedWorkflow = workflowRepository.save(createWorkFlowFromTemplate);
        savedWorkflow.setCurrentStep(savedWorkflow.getWorkflowSteps().getFirst().getWorkflowStepId());
        workflowRepository.save(savedWorkflow);
        return workflowTemplateMapper.toCustomWorkflowResponse(savedWorkflow);
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

        List<Workflow> workflows = workflowRepository.findByCreatedBy_Department(department).orElseThrow(() ->  new EntityNotFoundException("No workflow created for Department " + department));
        return workflows;
    }

    @Override
    @Transactional
    public void deleteWorkflowStep(Integer workflowId, Integer workflowStepId) {
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        Integer user = principal.user().getUserId();
        Workflow workflow = workflowRepository.findByWorkflowIdAndCreatedBy_UserId(workflowId, user).orElseThrow(() ->  new EntityNotFoundException("Workflow step with ID " + workflowStepId + " not found"));
        WorkflowStep stepToRemove = workflow.getWorkflowSteps()
                        .stream()
                .filter(step -> step.getWorkflowStepId().equals(workflowStepId))
                .toList()
                .getFirst();


        workflow.getWorkflowSteps().remove(stepToRemove);



    }

public List<ResponseGetWorkflowByIDDTO> getWorkflowByUserId() {
    var auth  = SecurityContextHolder.getContext().getAuthentication();
    var principal = (UserPrincipal) auth.getPrincipal();
    assert principal != null;
    Integer user = principal.user().getUserId();
        List<Workflow> workflowsCreatedByUser = workflowRepository.findByCreatedBy_UserId(user);
        return  workflowsCreatedByUser.stream().map(el -> {
            List<WorkflowStep> orderedSteps = workflowStepsRepository.findByWorkflow_WorkflowIdOrderByStepAsc(el.getWorkflowId());

            // 3. Attach the ordered list back to the workflow object (optional, for the return DTO)
            el.setWorkflowSteps(orderedSteps);
            return  workflowTemplateMapper.toGetByIdDto(el);
        }).toList();

}


    @Transactional
    public void updateWorkflowStepStatus(Integer workflowStepId,WorkflowStepUpdateStatusReqDTO dto) {


        WorkflowStep workflowStep = workflowStepsRepository.findById(workflowStepId)
                .orElseThrow(() -> new EntityNotFoundException("Workflow step not found"));




        boolean hasOpenTasks = workflowStep.getTask().stream()
                .anyMatch(t -> {
                    System.out.println(t.getTaskId());
                    return  !TaskStatus.COMPLETED.equals(t.getStatus());
                });
System.out.println(hasOpenTasks + "open staskas");
        if (hasOpenTasks) {
            throw new WorkflowUpdateException("Workflow Step has some open tasks");
        }


        switch (dto.getStepStatus()) {

            case WorkflowStepStatus.APPROVED -> {
                var auth  = SecurityContextHolder.getContext().getAuthentication();
                var principal = (UserPrincipal) auth.getPrincipal();
                Integer user = principal.user().getUserId();
                workflowStep.setStatus(WorkflowStepStatus.APPROVED);
                Integer actorId = principal.user().getUserId();
                Integer receiverId = workflowStep.getWorkflow().getCreatedBy().getUserId();

                if (!receiverId.equals(actorId)) {
                    eventPublisher.publishEvent(
                            new WorkflowEvent(
                                    receiverId,
                                    NotificationType.TASK_APPROVED,
                                    "Workflow step approved: " + workflowStep.getStepName()
                            )
                    );
                }

            }
            case WorkflowStepStatus.REJECTED -> {
                var auth  = SecurityContextHolder.getContext().getAuthentication();
                var principal = (UserPrincipal) auth.getPrincipal();
                Integer user = principal.user().getUserId();
                Task taskToUpdate = workflowStep
                        .getTask()
                        .stream()
                        .filter(task -> task.getTaskId().equals(dto.getTaskId()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Task Id " + dto.getTaskId() + "not linked to workflow step"));
                Comment comment = new Comment();
                comment.setTaskId(taskToUpdate.getTaskId().longValue());
                comment.setText(dto.getMessage());
                comment.setUserId(taskToUpdate.getAssignedToUser().getUserId());
                taskToUpdate.setStatus(TaskStatus.IN_PROGRESS);
//                taskRepository.save(taskToUpdate);
                workflowStep.setStatus(WorkflowStepStatus.REJECTED);
//                workflowStepsRepository.save(workflowStep);
                commentRepository.save(comment);
                Integer actorId = principal.user().getUserId();
                Integer receiverId = taskToUpdate.getAssignedToUser().getUserId();

                if (!receiverId.equals(actorId)) {
                    eventPublisher.publishEvent(
                            new WorkflowEvent(
                                    receiverId,
                                    NotificationType.TASK_REJECTED,
                                    "Task rejected in workflow: " + taskToUpdate.getTitle()
                            )
                    );
                }


            }
            case PENDING -> {
                workflowStep.setStatus(WorkflowStepStatus.PENDING);
            }
        }

        Workflow workflow = workflowStep.getWorkflow();
        if((workflow.getStatus().equals(WorkflowStatus.PENDING) || workflow.getStatus().equals(WorkflowStatus.COMPLETED)) && workflow.getWorkflowSteps().stream().anyMatch(el -> el.getTask().stream().anyMatch(e -> e.getStatus().equals(TaskStatus.IN_PROGRESS) || e.getStatus().equals(TaskStatus.COMPLETED) ))) {
            workflow.setStatus(WorkflowStatus.INPROGRESS);
        }
        if( workflow.getCurrentStep() <= workflow.getWorkflowSteps().size() ) {

            WorkflowStep nextStep =  workflow.getWorkflowSteps().stream().filter(step -> step.getStep().equals(workflowStep.getStep() + 1)).toList().getFirst();

            workflow.setCurrentStep(nextStep.getWorkflowStepId());
            workflowRepository.save(workflow);
        }

        if(workflow.getWorkflowSteps().stream().allMatch(el -> el.getStatus().equals(WorkflowStepStatus.APPROVED))) {
            workflow.setStatus(WorkflowStatus.COMPLETED);
        }


    }


}
