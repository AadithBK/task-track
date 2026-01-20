package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.TaskCreateDto;
import com.workflowmanagement.task_track.dto.TaskDto;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.service.TaskService;
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
    public ResponseEntity<TaskCreateDto> createTask(@Valid @RequestBody TaskCreateDto taskCreateDto){
        TaskCreateDto createdTask = taskService.createTask(taskCreateDto);
        return new ResponseEntity<>(createdTask , HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDto>> getAllTasks(){
        List<TaskDto> taskList = taskService.getAllTasks();
        return ResponseEntity.ok(taskList);
    }

    @GetMapping("createdby/{id}")
    public ResponseEntity<List<TaskDto>> getAllTasksByCreator(@Valid @PathVariable int id){
        List<TaskDto> taskList = taskService.getTasksByCreatorId(id);
        return ResponseEntity.ok(taskList);
    }

    @GetMapping("my")
    public ResponseEntity<List<TaskDto>> getAllTasksForUser(){
        List<TaskDto> taskList = taskService.getTasksAssignedToUser();
        return ResponseEntity.ok(taskList);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<TaskDto> updateTaskById(@Valid @PathVariable Integer id , @RequestBody TaskDto taskDto){
        return ResponseEntity.ok(taskService.updateTaskById(taskDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id)
    {
        if(taskService.deleteTaskById(id))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
