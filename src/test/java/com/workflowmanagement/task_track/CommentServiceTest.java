package com.workflowmanagement.task_track;

import com.workflowmanagement.task_track.dto.CommentDto;
import com.workflowmanagement.task_track.enums.Role; // Using the userEnum version
import com.workflowmanagement.task_track.model.Comment;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.repository.CommentRepository;
import com.workflowmanagement.task_track.repository.TaskRepository;
import com.workflowmanagement.task_track.service.implementation.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CommentService commentService;

    private User currentUser;
    private UserPrincipal userPrincipal;
    private Task task;

    @BeforeEach
    void setUp() {
        // 1. Setup the User with EMPLOYEE role (representing the non-manager user)
        currentUser = new User();
        currentUser.setUserId(1);
        currentUser.setUserName("testUser");
        currentUser.setRole(Role.EMPLOYEE); // Use EMPLOYEE from your Role enum

        // 2. Setup the UserPrincipal
        userPrincipal = new UserPrincipal(currentUser);

        // 3. Setup the Security Context Mocking
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        // 4. Setup a default Task
        task = new Task();
        task.setTaskId(100);
        task.setAssignedToUser(currentUser);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addComment_Success_AsAssignedEmployee() {
        // Arrange
        CommentDto dto = new CommentDto();
        dto.setTaskId(100L);
        dto.setText("Comment from assigned employee");

        when(taskRepository.findById(100)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Comment result = commentService.addComment(dto);

        // Assert
        assertNotNull(result);
        assertEquals(Role.EMPLOYEE.name(), currentUser.getRole().name()); // Verify role name
        assertEquals(1, result.getUserId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void addComment_Success_AsManager() {
        // Arrange
        currentUser.setRole(Role.MANAGER); // Set to MANAGER

        User otherEmployee = new User();
        otherEmployee.setUserId(2);
        task.setAssignedToUser(otherEmployee); // Task NOT assigned to manager

        CommentDto dto = new CommentDto();
        dto.setTaskId(100L);
        dto.setText("Manager's feedback");

        when(taskRepository.findById(100)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Comment result = commentService.addComment(dto);

        // Assert
        assertNotNull(result);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void addComment_Fail_Unauthorized() {
        // Arrange
        User anotherUser = new User();
        anotherUser.setUserId(99);
        task.setAssignedToUser(anotherUser); // Task belongs to someone else

        currentUser.setRole(Role.EMPLOYEE); // I am just an employee

        CommentDto dto = new CommentDto();
        dto.setTaskId(100L);

        when(taskRepository.findById(100)).thenReturn(Optional.of(task));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                commentService.addComment(dto)
        );
        assertTrue(exception.getMessage().contains("Access Denied"));
    }

    @Test
    void getCommentsByTask_Success() {
        // Arrange
        when(taskRepository.findById(100)).thenReturn(Optional.of(task));
        when(commentRepository.findByTaskId(100L)).thenReturn(List.of(new Comment()));

        // Act
        List<Comment> results = commentService.getCommentsByTask(100L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }
}