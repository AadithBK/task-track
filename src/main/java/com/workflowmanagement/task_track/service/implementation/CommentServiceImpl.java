package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.repository.CommentRepository;
import com.workflowmanagement.task_track.service.ICommentService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;

    // Constructor injection (best practice in Spring Boot)
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment addComment(Comment comment) {
        // Save a new comment to the database
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByTask(Long taskId) {
        // Retrieve all comments linked to a specific task
        return commentRepository.findByTaskId(taskId);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        // Find a comment by its ID
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.orElse(null);
        // Alternatively, throw a custom exception if not found
    }

    @Override
    public void deleteComment(Long commentId) {
        // Delete a comment safely
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new RuntimeException("Comment not found with ID: " + commentId);
        }
    }
}
