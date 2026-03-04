
package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.dto.ResponseTaskCreateDto;
import com.workflowmanagement.task_track.dto.TaskCreateDto;
import com.workflowmanagement.task_track.dto.TaskDto;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import com.workflowmanagement.task_track.model.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ITaskService {

    /**
     * Creates a new task using the provided payload.
     * Expects valid assignedToUserId and createdByUserId inside TaskCreateDto.
     *
     * @param task the task creation payload
     * @return the same creation payload or a created representation (per your current impl returns TaskCreateDto)
     * @throws jakarta.persistence.EntityNotFoundException if user IDs don’t exist
     */
    TaskDto createTask(TaskCreateDto task);

    /**
     * @return all tasks mapped to TaskDto
     */
    List<TaskDto> getAllTasks();

    /**
     * Finds tasks created by a given user.
     *
     * @return list of TaskDto
     */
    List<TaskDto> getTasksByCreatorId();

    /**
     * Finds tasks assigned to the currently authenticated user (via SecurityContext).
     *
     * @return list of TaskDto
     */
    List<TaskDto> getTasksAssignedToUser();


    /**
     * Finds task by the id.
     *
     * @return TaskDto
     */
    TaskDto getTaskById(int id);
    /**
     * Updates a task by ID with the fields present in TaskDto.
     *
     * @param t TaskDto containing updates (must include taskId)
     * @return the same TaskDto (per your current impl)
     * @throws jakarta.persistence.EntityNotFoundException if taskId doesn’t exist
     */
    TaskDto updateTaskById(TaskDto t);

    /**
     * Soft-deletes a task by setting status to BLOCKED.
     *
     * @param id taskId
     * @return true if status changed and saved, false if task not found
     */
    boolean deleteTaskById(Integer id);


    @Transactional
    @Modifying
    TaskDto updateTaskStatus(Integer taskId, TaskStatus taskStatus);
}

