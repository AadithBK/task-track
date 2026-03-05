package com.workflowmanagement.task_track;

import com.workflowmanagement.task_track.dto.subTask.SubTaskCreateRequestDTO;
import com.workflowmanagement.task_track.dto.subTask.SubTaskUpdateReqDTO;
import com.workflowmanagement.task_track.enums.subTaskEnum.SubTaskStatusEnum;
import com.workflowmanagement.task_track.model.SubTask;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.repository.SubTaskRepository;
import com.workflowmanagement.task_track.repository.TaskRepository;
import com.workflowmanagement.task_track.repository.UserRepository;
import com.workflowmanagement.task_track.service.implementation.SubTaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubTaskServiceTest {

    @Mock private SubTaskRepository subTaskRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @InjectMocks
    private SubTaskService subTaskService;

    private User currentUser;
    private Task parentTask;
    private SubTask existingSubTask;

    @BeforeEach
    void setUp() {
        // Setup Users
        currentUser = new User();
        currentUser.setUserId(1);
        currentUser.setEmail("creator@test.com");

        // Setup Parent Task
        parentTask = new Task();
        parentTask.setTaskId(10);
        parentTask.setCreatedByUser(currentUser); // Current user is the creator
        parentTask.setAssignedToUser(currentUser);

        // Setup Existing SubTask
        existingSubTask = new SubTask();
        existingSubTask.setSubTaskId(100);
        existingSubTask.setTaskId(parentTask);
        existingSubTask.setAssignedToUserId(currentUser);
        existingSubTask.setStatus(SubTaskStatusEnum.OPEN);

        // Mock Security Context
        UserPrincipal principal = new UserPrincipal(currentUser);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void save_Success() {
        // Arrange
        SubTaskCreateRequestDTO dto = new SubTaskCreateRequestDTO(10, "New Subtask", SubTaskStatusEnum.OPEN);
        when(taskRepository.findById(10)).thenReturn(Optional.of(parentTask));
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(existingSubTask);

        // Act
        SubTask result = subTaskService.save(dto);

        // Assert
        assertNotNull(result);
        verify(subTaskRepository).save(any(SubTask.class));
    }

    @Test
    void updateSubTask_Success_WhenUserIsCreator() {
        // Arrange
        SubTaskUpdateReqDTO updateDto = new SubTaskUpdateReqDTO("Updated Title", SubTaskStatusEnum.INPROGRESS, 1);

        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));
        when(taskRepository.findById(10)).thenReturn(Optional.of(parentTask));
        when(subTaskRepository.findBySubTaskIdAndTaskId(100, parentTask)).thenReturn(Optional.of(existingSubTask));

        // Act
        SubTask result = subTaskService.updateSubTask(updateDto, 10, 100);

        // Assert
        assertEquals("Updated Title", result.getTitle());
        assertEquals(SubTaskStatusEnum.INPROGRESS, result.getStatus());
    }

    @Test
    void updateSubTask_ThrowsException_WhenUserIsNotCreator() {
        // Arrange
        User differentUser = new User();
        differentUser.setUserId(2); // ID 2 vs Current User ID 1
        parentTask.setCreatedByUser(differentUser);

        SubTaskUpdateReqDTO updateDto = new SubTaskUpdateReqDTO("Title", SubTaskStatusEnum.OPEN, 2);

        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));
        when(taskRepository.findById(10)).thenReturn(Optional.of(parentTask));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                subTaskService.updateSubTask(updateDto, 10, 100)
        );
    }

    @Test
    void deleteSubTask_Success() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));
        when(taskRepository.findById(10)).thenReturn(Optional.of(parentTask));
        when(subTaskRepository.findBySubTaskIdAndTaskId(100, parentTask)).thenReturn(Optional.of(existingSubTask));

        // Act
        subTaskService.deleteSubTask(10, 100);

        // Assert
        verify(subTaskRepository).deleteById(100);
    }

    @Test
    void updateSubTaskStatus_DirectlyByStatus() {
        // Arrange
        when(subTaskRepository.findBySubTaskIdAndTaskId_TaskId(100, 10)).thenReturn(Optional.of(existingSubTask));

        // Act
        subTaskService.updateSubTaskStatus(10, 100, SubTaskStatusEnum.COMPLETED);

        // Assert
        assertEquals(SubTaskStatusEnum.COMPLETED, existingSubTask.getStatus());
    }
}