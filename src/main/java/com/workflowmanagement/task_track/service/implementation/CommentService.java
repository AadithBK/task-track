package com.workflowmanagement.task_track.service.implementation;
import com.workflowmanagement.task_track.dto.CommentDto;
import com.workflowmanagement.task_track.model.UserPrincipal;
import org.springframework.context.ApplicationEventPublisher;
import com.workflowmanagement.task_track.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.workflowmanagement.task_track.model.Comment;
import com.workflowmanagement.task_track.repository.CommentRepository;
import com.workflowmanagement.task_track.service.ICommentService;
import com.workflowmanagement.task_track.event.WorkflowEvent;
import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import com.workflowmanagement.task_track.model.Task;
import java.util.List;

@Service
@RequiredArgsConstructor // Using Lombok to handle constructor injection for both repositories
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository; // Required to check assignment
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public Comment addComment(CommentDto commentDTO) {
        // 1. Get current authenticated user and their Role
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Integer currentUserId = userPrincipal.user().getUserId();
        String currentUserName = userPrincipal.user().getUserName();

        // Get the role (assuming your User entity or Principal has a getRole() method)
        String userRole = userPrincipal.user().getRole().name();

        // 2. Fetch the Task to check assignment
        Task task = taskRepository.findById(Math.toIntExact(commentDTO.getTaskId()))
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + commentDTO.getTaskId()));

        // 3. Authorization Logic
        boolean isManager = userRole.equals("MANAGER");
        boolean isAssignedUser = task.getAssignedToUser().getUserId().equals(currentUserId);

        if (!isManager && !isAssignedUser) {
            throw new RuntimeException("Access Denied: You are not assigned to this task.");
        }

        // 4. Save the comment if authorized
        Comment comment = new Comment();
        comment.setTaskId(commentDTO.getTaskId());
        comment.setText(commentDTO.getText());
        comment.setUserId(currentUserId);
        comment.setAuthorName(currentUserName);

            Comment savedComment = commentRepository.save(comment);

// Determine receivers
            Integer creatorId = task.getCreatedByUser().getUserId();
            Integer assigneeId = task.getAssignedToUser().getUserId();

// Notify creator (if not commenter)
            if (!creatorId.equals(currentUserId)) {
                eventPublisher.publishEvent(
                        new WorkflowEvent(
                                creatorId,
                                NotificationType.TASK_STATUS_UPDATE,
                                "New comment by " + userPrincipal.user().getUserName()+ "on task:"+ task.getTitle()
                        )
                );
            }

// Notify assignee (if not commenter)
            if (!assigneeId.equals(currentUserId)) {
                eventPublisher.publishEvent(
                        new WorkflowEvent(
                                assigneeId,
                                NotificationType.TASK_STATUS_UPDATE,
                                "New comment by " + userPrincipal.user().getUserName()+ "on task:"+ task.getTitle()
                        )
                );
            }

            return savedComment;
        }





    @Override
    public List<Comment> getCommentsByTask(Long taskId) {
        // 1. Get current authenticated user and their Role
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Integer currentUserId = userPrincipal.user().getUserId();

        // Get the role
        String userRole = userPrincipal.user().getRole().name();

        // 2. Fetch the Task to check assignment
        // Using Math.toIntExact to match your addComment implementation
        Task task = taskRepository.findById(Math.toIntExact(taskId))
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        // 3. Authorization Logic
        boolean isManager = userRole.equals("MANAGER");
        boolean isAssignedUser = task.getAssignedToUser().getUserId().equals(currentUserId);

        if (!isManager && !isAssignedUser) {
            throw new RuntimeException("Access Denied: You cannot view comments for a task not assigned to you.");
        }

        // 4. Return the list of comments if authorized
        return commentRepository.findByTaskId(taskId);
    }
}

//    @Override
//    public List<Comment> getCommentsByTask(Long taskId) {
//        return commentRepository.findByTaskId(taskId);
//    }
//}




//@Service
//public class CommentService implements ICommentService {
//
//    private final CommentRepository commentRepository;
//
//    // Constructor injection (best practice)
//    public CommentService(CommentRepository commentRepository) {
//        this.commentRepository = commentRepository;
//    }
//
//
//        @Override
//        public Comment addComment(CommentDto commentDTO) {
//            // Get current authenticated user ID
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            var userPrincipal =  (UserPrincipal) auth.getPrincipal();
////            Integer currentUserId = Integer.valueOf(auth.getUserId());
//            Integer currentUserId = userPrincipal.user().getUserId();
//            // assuming username is userId, otherwise map properly
//
//            Comment comment = new Comment();
//            comment.setTaskId(commentDTO.getTaskId());
//            comment.setText(commentDTO.getText());
//            comment.setUserId(currentUserId);
//
//            return commentRepository.save(comment);
//        }
//
//
//    @Override
//    public List<Comment> getCommentsByTask(Long taskId) {
//
//
//        return commentRepository.findByTaskId(taskId);
//    }

//    @Override
//    public Comment getCommentById(Long commentId) {
//        Optional<Comment> comment = commentRepository.findById(commentId);
//        return comment.orElse(null);
//        // You can throw a custom exception instead of returning null
//    }

//    @Override
//    public void deleteComment(Long commentId) {
//        if (commentRepository.existsById(commentId)) {
//            commentRepository.deleteById(commentId);
//        } else {
//            throw new RuntimeException("Comment not found with ID: " + commentId);
//        }
//    }
//}
