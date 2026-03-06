package com.workflowmanagement.task_track;

import com.workflowmanagement.task_track.dto.TaskCreateDto;
import com.workflowmanagement.task_track.dto.TaskDto;
import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import com.workflowmanagement.task_track.event.WorkflowEvent;
import com.workflowmanagement.task_track.mapper.TaskMapper;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.model.workflow.WorkflowStep;
import com.workflowmanagement.task_track.repository.TaskRepository;
import com.workflowmanagement.task_track.repository.UserRepository;
import com.workflowmanagement.task_track.repository.WorkflowStepsRepository;
import com.workflowmanagement.task_track.service.implementation.TaskService;
import com.workflowmanagement.task_track.service.implementation.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private TaskMapper taskMapper;
    @Mock private UserRepository userRepository;
    @Mock private UserService userService;
    @Mock private TaskRepository taskRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private WorkflowStepsRepository workflowStepsRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock private Authentication authentication;
    @Mock private SecurityContext securityContext;

    private User currentUser;
    private User assignedUser;
    private Task testTask;
    private WorkflowStep testStep;

    @BeforeEach
    void setUp() {
        // Setup Current User (The Creator)
        currentUser = new User();
        currentUser.setUserId(100);
        currentUser.setCreatedTasks(new ArrayList<>());

        // Setup Assigned User
        assignedUser = new User();
        assignedUser.setUserId(200);
        assignedUser.setAssignedTasks(new ArrayList<>());

        // Setup Workflow Step
        testStep = new WorkflowStep();
        testStep.setStep(1);

        // Setup Security Context Mocking
        UserPrincipal principal = new UserPrincipal(currentUser);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createTask_Success() {
        // Arrange
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Test Task");
        dto.setAssignedToUserId(200);
        dto.setWorkflowStepId(1);

        when(userService.getUserObjById(200)).thenReturn(Optional.of(assignedUser));
        when(userService.getUserObjById(100)).thenReturn(Optional.of(currentUser));
        when(workflowStepsRepository.findById(1)).thenReturn(Optional.of(testStep));

        // Act
        taskService.createTask(dto);

        // Assert
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(eventPublisher, times(1)).publishEvent(any(WorkflowEvent.class));

        // Verify relationship bi-directionality
        assertFalse(currentUser.getCreatedTasks().isEmpty());
        assertFalse(assignedUser.getAssignedTasks().isEmpty());
    }

    @Test
    void createTask_ThrowsException_WhenStepNotFound() {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setWorkflowStepId(99);

        when(workflowStepsRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.createTask(dto));
    }

    @Test
    void updateTaskStatus_Success() {
        // Arrange
        Task task = new Task();
        task.setTaskId(1);
        task.setStatus(TaskStatus.OPEN);
        task.setSubTasks(new ArrayList<>()); // Empty subtasks means we can complete it

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        // Act
        taskService.updateTaskStatus(1, TaskStatus.IN_PROGRESS);

        // Assert
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void deleteTaskById_ChangesStatusToBlocked() {
        // Arrange
        Task task = new Task();
        task.setTaskId(1);
        task.setStatus(TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        // Act
        boolean result = taskService.deleteTaskById(1);

        // Assert
        assertTrue(result);
        assertEquals(TaskStatus.BLOCKED, task.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    void getTasksAssignedToUser_Success() {
        // Act
        taskService.getTasksAssignedToUser();

        // Assert
        // Verify that it uses the ID from the SecurityContext (100)
        verify(taskRepository).findByAssignedToUser_UserId(100);
    }
}