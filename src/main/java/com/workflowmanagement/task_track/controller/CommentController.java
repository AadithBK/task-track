package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.CommentDto;
import com.workflowmanagement.task_track.enums.CustomStatus;
import com.workflowmanagement.task_track.model.Comment;
import com.workflowmanagement.task_track.service.implementation.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Add a new comment
    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody CommentDto commentDTO) {
        return new ResponseEntity<>(commentService.addComment(commentDTO), HttpStatus.CREATED); }

    // Get all comments for a task
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Comment>> getCommentsByTask(@PathVariable Long taskId) {

        List<Comment> comments=commentService.getCommentsByTask(taskId);
        if(comments.isEmpty())
        {
            return new ResponseEntity<>(comments,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comments,HttpStatus.OK);

    }
}
