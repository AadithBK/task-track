package com.workflowmanagement.task_track.controller;


import com.workflowmanagement.task_track.dto.subTask.SubTaskCreateRequestDTO;
import com.workflowmanagement.task_track.dto.subTask.SubTaskUpdateReqDTO;
import com.workflowmanagement.task_track.model.SubTask;

import com.workflowmanagement.task_track.service.SubTaskService;
import com.workflowmanagement.task_track.util.CustomJSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task/sub")
public class SubtaskController {
    @Autowired
    private SubTaskService subTasksService;

    @PostMapping("")
    public ResponseEntity<CustomJSONResponse<SubTask>> createSubTask(@RequestBody SubTaskCreateRequestDTO subTask) {
            SubTask createdSubTask = subTasksService.save(subTask);
            System.out.println(createdSubTask);
            return new ResponseEntity<CustomJSONResponse<SubTask>>(CustomJSONResponse.ok("Sub Task Created", createdSubTask), HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<CustomJSONResponse<List<SubTask>>> getAllSubTasksForTask(@PathVariable Integer taskId) {

        return new ResponseEntity<CustomJSONResponse<List<SubTask>>>(CustomJSONResponse.ok(null, subTasksService.getAllSubTasks(taskId)), HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}/{subTaskId}")
    public ResponseEntity<CustomJSONResponse<SubTask>> getAllSubTasksForTask(@RequestBody SubTaskUpdateReqDTO subTaskUpdateReqDTO, @PathVariable Integer taskId, @PathVariable Integer subTaskId) {

        return new ResponseEntity<CustomJSONResponse<SubTask>>(CustomJSONResponse.ok("Updated sub task", subTasksService.updateSubTask(subTaskUpdateReqDTO, taskId, subTaskId)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{taskId}/{subTaskId}")
    public  ResponseEntity deleteSubTask(@PathVariable Integer taskId, @PathVariable Integer subTaskId) {
        subTasksService.deleteSubTask(taskId, subTaskId);
        return  new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{taskId}")
    public  ResponseEntity deleteAllSubTask(@PathVariable Integer taskId) {
        subTasksService.deleteAllSubTask(taskId);
        return  new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
