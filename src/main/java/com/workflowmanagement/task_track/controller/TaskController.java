package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.TaskCreateDto;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/createtask")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskCreateDto taskCreateDto){
        Task createdTask = taskService.createTask(taskCreateDto);
        return new ResponseEntity<>(createdTask , HttpStatus.CREATED);
    }

    @GetMapping("/getalltasks")
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> taskList = taskService.getAllTasks();
        return ResponseEntity.ok(taskList);
    }

    @GetMapping("/gettasksbycreator/{id}")
    public ResponseEntity<List<Task>> getAllTasksByCreator(@Valid @PathVariable int id){
        List<Task> taskList = taskService.getTasksByCreatorId(id);
        return ResponseEntity.ok(taskList);
    }

    @GetMapping("/gettasksofuser/{id}")
    public ResponseEntity<List<Task>> getAllTasksForUser(@Valid @PathVariable int id){
        List<Task> taskList = taskService.getTasksAssignedToUser(id);
        return ResponseEntity.ok(taskList);
    }


}
