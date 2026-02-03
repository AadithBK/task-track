package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.TaskCreateDto;
import com.workflowmanagement.task_track.dto.TaskDto;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.service.implementation.TaskService;
import com.workflowmanagement.task_track.util.CustomJSONResponse;

import com.workflowmanagement.task_track.enums.CustomStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task/")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("create")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CustomJSONResponse<TaskDto>> createTask(@Valid @RequestBody TaskCreateDto taskCreateDto) {
        TaskDto createdTask = taskService.createTask(taskCreateDto);

        return new ResponseEntity<>(CustomJSONResponse.ok(null,createdTask), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomJSONResponse<List<TaskDto>>> getAllTasks() {
        List<TaskDto> taskList = taskService.getAllTasks();
        return new ResponseEntity<>(CustomJSONResponse.ok("tasks fetched successfully",
                taskList), HttpStatus.OK);
    }

    @GetMapping("createdby/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CustomJSONResponse<List<TaskDto>>> getAllTasksByCreator(@Valid @PathVariable int id) {
        List<TaskDto> taskList = taskService.getTasksByCreatorId(id);
        return new ResponseEntity<>(CustomJSONResponse.ok("tasks fetched successfully",  taskList), HttpStatus.OK);
    }

    @GetMapping("my")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER')")
    public ResponseEntity<CustomJSONResponse<List<TaskDto>>> getAllTasksForUser() {
        List<TaskDto> taskList = taskService.getTasksAssignedToUser();
        return new ResponseEntity<>(CustomJSONResponse.ok("tasks fetched successfully",  taskList), HttpStatus.OK);
    }


    @PutMapping("update/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CustomJSONResponse<TaskDto>> updateTaskById(@Valid @PathVariable Integer id, @RequestBody TaskDto taskDto) {
        TaskDto taskDto1 = taskService.updateTaskById(taskDto);
        return new ResponseEntity<>(CustomJSONResponse.ok(null, taskDto1), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CustomJSONResponse<Void>> deleteUserById(@PathVariable Integer id) {
        if (taskService.deleteTaskById(id)) {

            return new ResponseEntity<>(CustomJSONResponse.ok("Task modified successfully", null),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/count/completed/user/{userId}")
    @PreAuthorize("hasRole('MANAGER')")
    public long getCompletedCountByUser(@PathVariable Integer userId) {
        return taskService.getCompletedCountByUser(userId);
    }

    @GetMapping("/count/total/user/{userId}")
    @PreAuthorize("hasRole('MANAGER')")
    public long getTotalCountByUser(@PathVariable Integer userId) {
        return taskService.getTotalCountByUser(userId);
    }

    @GetMapping("/count/completed/dept/{deptName}")
    @PreAuthorize("hasRole('MANAGER')")
    public long getCompletedCountByDept(@PathVariable String deptName) {
        return taskService.getCompletedCountByDept(deptName);
    }

    @GetMapping("/count/total/dept/{deptName}")
    @PreAuthorize("hasRole('MANAGER')")
    public long getTotalCountByDept(@PathVariable String deptName) {
        return taskService.getTotalCountByDept(deptName);
    }




}
