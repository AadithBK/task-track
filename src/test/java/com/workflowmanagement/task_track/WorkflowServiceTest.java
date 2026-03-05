package com.workflowmanagement.task_track;

import com.workflowmanagement.task_track.dto.workflow.ResponseCreateNewWorkflowDTO;
import com.workflowmanagement.task_track.dto.workflow.WorkflowStepUpdateStatusReqDTO;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStatus;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import com.workflowmanagement.task_track.exceptions.WorkflowUpdateException;
import com.workflowmanagement.task_track.mapper.WorkflowTemplateMapper;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.model.workflow.Workflow;
import com.workflowmanagement.task_track.model.workflow.WorkflowStep;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;
import com.workflowmanagement.task_track.repository.*;
import com.workflowmanagement.task_track.service.implementation.WorkflowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowServiceTest {

    @Mock private WorkflowRepository workflowRepository;
    @Mock private WorkflowTemplateRepository workflowTemplateRepository;
    @Mock private WorkflowTemplateMapper workflowTemplateMapper;
    @Mock private UserRepository userRepository;
    @Mock private WorkflowStepsRepository workflowStepsRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private CommentRepository commentRepository;

    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @InjectMocks
    private WorkflowService workflowService;

    private User testUser;
    private WorkflowTemplate testTemplate;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);

        // Mock Security
        UserPrincipal principal = new UserPrincipal(testUser);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(securityContext);

        // Setup Template
        testTemplate = new WorkflowTemplate();
        testTemplate.setWorkflowTemplateId(10);
        testTemplate.setWorkflowTemplateName("Test Template");

        WorkflowTemplateStep step1 = new WorkflowTemplateStep();
        step1.setWorkflowTemplateStepName("Step 1");
        step1.setWorkflowTemplateStepPosition(1);

        testTemplate.setWorkflowSteps(List.of(step1));
    }

    @Test
    void createWorkflowFromTemplate_Success() {
        // Arrange
        when(workflowTemplateRepository.findById(10)).thenReturn(Optional.of(testTemplate));
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(workflowRepository.save(any(Workflow.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        workflowService.createWorkflowFromTemplate(10);

        // Assert
        verify(workflowRepository, times(2)).save(any(Workflow.class));
        verify(workflowTemplateMapper).toCustomWorkflowResponse(any(Workflow.class));
    }

    @Test
    void updateWorkflowStepStatus_ThrowsException_IfTasksNotCompleted() {
        // Arrange
        WorkflowStep step = new WorkflowStep();
        Task openTask = new Task();
        openTask.setStatus(TaskStatus.IN_PROGRESS); // Not COMPLETED
        step.setTask(List.of(openTask));

        when(workflowStepsRepository.findById(100)).thenReturn(Optional.of(step));
        WorkflowStepUpdateStatusReqDTO dto = new WorkflowStepUpdateStatusReqDTO();
        dto.setStepStatus(WorkflowStepStatus.APPROVED);

        // Act & Assert
        assertThrows(WorkflowUpdateException.class, () ->
                workflowService.updateWorkflowStepStatus(100, dto)
        );
    }

    @Test
    void updateWorkflowStepStatus_Approve_MovesToNextStep() {
        // Arrange
        Workflow workflow = new Workflow();
        workflow.setStatus(WorkflowStatus.PENDING);

        WorkflowStep currentStep = new WorkflowStep();
        currentStep.setWorkflowStepId(1); // Changed from 100 to 1
        currentStep.setStep(1);           // Position 1
        currentStep.setTask(new ArrayList<>());
        currentStep.setWorkflow(workflow);
        currentStep.setStatus(WorkflowStepStatus.PENDING);

        WorkflowStep nextStep = new WorkflowStep();
        nextStep.setWorkflowStepId(2); // Changed from 101 to 2
        nextStep.setStep(2);           // Position 2
        nextStep.setTask(new ArrayList<>());
        nextStep.setWorkflow(workflow);
        nextStep.setStatus(WorkflowStepStatus.PENDING);

        workflow.setWorkflowSteps(List.of(currentStep, nextStep));
        workflow.setCurrentStep(1); // Start at ID 1

        when(workflowStepsRepository.findById(1)).thenReturn(Optional.of(currentStep));

        WorkflowStepUpdateStatusReqDTO dto = new WorkflowStepUpdateStatusReqDTO();
        dto.setStepStatus(WorkflowStepStatus.APPROVED);

        // Act
        workflowService.updateWorkflowStepStatus(1, dto);

        // Assert
        assertEquals(WorkflowStepStatus.APPROVED, currentStep.getStatus());
        assertEquals(2, workflow.getCurrentStep()); // Now 1 <= 2 is TRUE, so it updates to 2
        verify(workflowRepository).save(workflow);
    }

    @Test
    void updateWorkflowStepStatus_Reject_CreatesComment() {
        // 1. Create a Mock/Real User (The missing piece!)
        User testUser = new User();
        testUser.setUserId(1);
        testUser.setUserName("test_user");

        // 2. Setup Step 1 (The step being rejected)
        WorkflowStep step1 = new WorkflowStep();
        step1.setWorkflowStepId(100);
        step1.setStep(1);
        step1.setStatus(WorkflowStepStatus.PENDING);
        step1.setTask(new ArrayList<>());

        // 3. Setup the Task AND assign the User to it
        Task task = new Task();
        task.setTaskId(500);
        task.setStatus(TaskStatus.COMPLETED); // Pass the "open tasks" check
        task.setAssignedToUser(testUser);     // FIXES THE CURRENT NPE
        step1.getTask().add(task);

        // 4. Setup Step 2 (So the Stream findFirst/getFirst doesn't fail)
        WorkflowStep step2 = new WorkflowStep();
        step2.setWorkflowStepId(101);
        step2.setStep(2);
        step2.setStatus(WorkflowStepStatus.PENDING);
        step2.setTask(new ArrayList<>());

        // 5. Setup the Workflow
        Workflow workflow = new Workflow();
        workflow.setStatus(WorkflowStatus.PENDING); // Pass the Workflow Status check
        workflow.setWorkflowSteps(new ArrayList<>(List.of(step1, step2)));
        workflow.setCurrentStep(100);

        step1.setWorkflow(workflow);
        step2.setWorkflow(workflow);

        // 6. Mock Repository behaviors
        when(workflowStepsRepository.findById(100)).thenReturn(Optional.of(step1));
        // If your service uses the user repo, mock it here:
        // when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // 7. Create DTO
        WorkflowStepUpdateStatusReqDTO dto = new WorkflowStepUpdateStatusReqDTO();
        dto.setStepStatus(WorkflowStepStatus.REJECTED);
        dto.setTaskId(500);
        dto.setMessage("Needs more work");

        // Act
        workflowService.updateWorkflowStepStatus(100, dto);

        // Assert
        assertEquals(WorkflowStepStatus.REJECTED, step1.getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus()); // Verify it reverted
        verify(commentRepository).save(any());
    }
}