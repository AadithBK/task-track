package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.dto.TaskCreateDto;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.repository.TaskRepository;
import com.workflowmanagement.task_track.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TaskService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(TaskCreateDto task){

        Optional<User> optAssignedUser = userService.getUserById(task.getAssignedToUserId());
        Optional<User> optCreatedUser = userService.getUserById(task.getCreatedByUserId());

        if (optAssignedUser.isPresent() && optCreatedUser.isPresent() ){
            User assignedToUser = optAssignedUser.get();
            User createdByUser = optCreatedUser.get();
            Task createdTask = Task.builder()
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .assignedToUser(assignedToUser)
                    .createdByUser(createdByUser)
                    .status(task.getStatus())
                    .priority(task.getPriority())
                    .dueDate(task.getDueDate())
                    .build();
            assignedToUser.getAssignedTasks().add(createdTask);
            createdByUser.getCreatedTasks().add(createdTask);
            taskRepository.save(createdTask);
            return createdTask;
        }
        else {
            throw new EntityNotFoundException("The given user id does not match with the records");
        }

    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }


    public List<Task> getTasksByCreatorId(Integer id){
        return taskRepository.findByCreatedByUser_UserId(id);
    }

    public List<Task> getTasksAssignedToUser(Integer id){
        return taskRepository.findByAssignedToUser_UserId(id);
    }
}
