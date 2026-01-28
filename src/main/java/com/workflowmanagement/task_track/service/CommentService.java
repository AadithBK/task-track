package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.model.Comment;

import java.util.List;

public interface ICommentService {

    /**
     * Add a new comment to a task.
     * @param comment the Comment object to be saved
     * @return the saved Comment
     */
    Comment addComment(Comment comment);

    /**
     * Get all comments for a specific task.
     * @param taskId the ID of the task
     * @return list of comments belonging to the task
     */
    List<Comment> getCommentsByTask(Long taskId);

    /**
     * Get a single comment by its ID.
     * @param commentId the ID of the comment
     * @return the Comment object if found
     */
    Comment getCommentById(Long commentId);

    /**
     * Delete a comment by its ID.
     * @param commentId the ID of the comment
     */
    void deleteComment(Long commentId);
}
