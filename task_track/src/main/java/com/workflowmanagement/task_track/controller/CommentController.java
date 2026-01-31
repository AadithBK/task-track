package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.CommentDTO;
import com.workflowmanagement.task_track.model.Comment;
import com.workflowmanagement.task_track.service.CommentService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Add a new comment
    @PostMapping
    public Comment addComment(@RequestBody CommentDTO commentDTO) {
        return commentService.addComment(commentDTO); }

    // Get all comments for a task
    @GetMapping("/task/{taskId}")
    public List<Comment> getCommentsByTask(@PathVariable Long taskId) {
        return commentService.getCommentsByTask(taskId);
    }
}
