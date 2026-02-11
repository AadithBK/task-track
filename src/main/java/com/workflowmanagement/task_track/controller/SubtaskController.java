package com.workflowmanagement.task_track.controller;


import com.workflowmanagement.task_track.dto.subTask.SubTaskCreateRequestDTO;
import com.workflowmanagement.task_track.dto.subTask.SubTaskPatchReqDTO;
import com.workflowmanagement.task_track.dto.subTask.SubTaskUpdateReqDTO;
import com.workflowmanagement.task_track.model.SubTask;

import com.workflowmanagement.task_track.service.implementation.SubTaskService;
import com.workflowmanagement.task_track.util.CustomJSONResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task/sub")

@Valid
public class SubtaskController {

    @Autowired
    private SubTaskService subTasksService;
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("")

    public ResponseEntity<CustomJSONResponse<SubTask>> createSubTask(@RequestBody SubTaskCreateRequestDTO subTask) {
            SubTask createdSubTask = subTasksService.save(subTask);
            System.out.println(createdSubTask);
            return new ResponseEntity<>(CustomJSONResponse.ok("Sub Task Created", createdSubTask), HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<CustomJSONResponse<List<SubTask>>> getAllSubTasksForTask(@PathVariable Integer taskId) {

        return new ResponseEntity<>(CustomJSONResponse.ok(null, subTasksService.getAllSubTasks(taskId)), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{taskId}/{subTaskId}")
    public ResponseEntity<CustomJSONResponse<SubTask>> getAllSubTasksForTask(@RequestBody SubTaskUpdateReqDTO subTaskUpdateReqDTO, @PathVariable Integer taskId, @PathVariable Integer subTaskId) {

        return new ResponseEntity<>(CustomJSONResponse.ok("Updated sub task", subTasksService.updateSubTask(subTaskUpdateReqDTO, taskId, subTaskId)), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{taskId}/{subTaskId}")
    public  ResponseEntity deleteSubTask(@PathVariable Integer taskId, @PathVariable Integer subTaskId) {
        subTasksService.deleteSubTask(taskId, subTaskId);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PatchMapping("/status/{taskId}/{subTaskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSubTaskStatus( @PathVariable Integer taskId, @PathVariable Integer subTaskId, @RequestBody SubTaskPatchReqDTO subTaskPatchReqDTO) {
            subTasksService.updateSubTaskStatus(taskId, subTaskId, subTaskPatchReqDTO.status());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void deleteAllSubTask(@PathVariable Integer taskId) {
        subTasksService.deleteAllSubTask(taskId);
    }
}
