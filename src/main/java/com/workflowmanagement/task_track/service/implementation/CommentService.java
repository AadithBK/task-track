package com.workflowmanagement.task_track.service.implementation;
import com.workflowmanagement.task_track.dto.CommentDto;
import com.workflowmanagement.task_track.model.UserPrincipal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.workflowmanagement.task_track.model.Comment;
import com.workflowmanagement.task_track.repository.CommentRepository;
import com.workflowmanagement.task_track.service.ICommentService;
import com.workflowmanagement.task_track.event.WorkflowEvent;
import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.repository.TaskRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

    // Constructor injection (best practice)
    public CommentService(CommentRepository commentRepository, ApplicationEventPublisher eventPublisher) {
        this.commentRepository = commentRepository;
        this.eventPublisher = eventPublisher;
    }


        @Override
        public Comment addComment(CommentDto commentDTO) {
            // Get current authenticated user ID
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var userPrincipal =  (UserPrincipal) auth.getPrincipal();
//            Integer currentUserId = Integer.valueOf(auth.getUserId());
            Integer currentUserId = userPrincipal.user().getUserId();
            // assuming username is userId, otherwise map properly

            Comment comment = new Comment();
            comment.setTaskId(commentDTO.getTaskId());
            comment.setText(commentDTO.getText());
            comment.setUserId(currentUserId);

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


        return commentRepository.findByTaskId(taskId);
    }

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
}
