package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.TaskCreateDto;
import com.workflowmanagement.task_track.dto.TaskDto;
import com.workflowmanagement.task_track.service.TaskService;
import com.workflowmanagement.task_track.util.CustomJSONResponse;
import com.workflowmanagement.task_track.util.CustomStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task/")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("create")
    public ResponseEntity<CustomJSONResponse<TaskCreateDto>> createTask(@Valid @RequestBody TaskCreateDto taskCreateDto) {
        TaskCreateDto createdTask = taskService.createTask(taskCreateDto);
        return new ResponseEntity<>(new CustomJSONResponse<>("", "", CustomStatus.OK, createdTask), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<CustomJSONResponse<List<TaskDto>>> getAllTasks() {
        List<TaskDto> taskList = taskService.getAllTasks();
        return new ResponseEntity<>(new CustomJSONResponse<>("tasks fetched successfully", "", CustomStatus.OK, taskList), HttpStatus.OK);
    }

    @GetMapping("createdby/{id}")
    public ResponseEntity<CustomJSONResponse<List<TaskDto>>> getAllTasksByCreator(@Valid @PathVariable int id) {
        List<TaskDto> taskList = taskService.getTasksByCreatorId(id);
        return new ResponseEntity<>(new CustomJSONResponse<>("tasks fetched successfully", "", CustomStatus.OK, taskList), HttpStatus.OK);
    }

    @GetMapping("my")
    public ResponseEntity<CustomJSONResponse<List<TaskDto>>> getAllTasksForUser() {
        List<TaskDto> taskList = taskService.getTasksAssignedToUser();
        return new ResponseEntity<>(new CustomJSONResponse<>("tasks fetched successfully", "", CustomStatus.OK, taskList), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CustomJSONResponse<TaskDto>> updateTaskById(@Valid @PathVariable Integer id, @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(new CustomJSONResponse<>("", "", CustomStatus.OK, taskDto), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomJSONResponse<Void>> deleteUserById(@PathVariable Integer id) {
        if (taskService.deleteTaskById(id)) {
            return new ResponseEntity<>(new CustomJSONResponse<>("Task modified successfully", "", CustomStatus.OK , null ),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
