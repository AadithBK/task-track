package com.workflowmanagement.task_track.service.implementation;
import com.workflowmanagement.task_track.dto.CommentDTO;
import com.workflowmanagement.task_track.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.workflowmanagement.task_track.model.Comment;
import com.workflowmanagement.task_track.repository.CommentRepository;
import com.workflowmanagement.task_track.service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    // Constructor injection (best practice)
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


        @Override
        public Comment addComment(CommentDTO commentDTO) {
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

            return commentRepository.save(comment);
        }


    @Override
    public List<Comment> getCommentsByTask(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.orElse(null);
        // You can throw a custom exception instead of returning null
    }

    @Override
    public void deleteComment(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new RuntimeException("Comment not found with ID: " + commentId);
        }
    }
}
