package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.dto.TaskCreateDto;
import com.workflowmanagement.task_track.dto.TaskDto;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import com.workflowmanagement.task_track.mapper.TaskMapper;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.repository.TaskRepository;
import com.workflowmanagement.task_track.repository.UserRepository;
import com.workflowmanagement.task_track.service.ITaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TaskRepository taskRepository;

    public TaskCreateDto createTask(TaskCreateDto task){

        Optional<User> optAssignedUser = userService.getUserObjById(task.getAssignedToUserId());
        Optional<User> optCreatedUser = userService.getUserObjById(task.getCreatedByUserId());

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
            return task;
        }
        else {
            throw new EntityNotFoundException("The given user id does not match with the records");
        }

    }

    public List<TaskDto> getAllTasks(){
        List<Task> taskList =  taskRepository.findAll();
        return taskMapper.toDtoList(taskList);
    }


    public List<TaskDto> getTasksByCreatorId(Integer id){
        List<Task> taskList =  taskRepository.findByCreatedByUser_UserId(id);
        return taskMapper.toDtoList(taskList);
    }

    public List<TaskDto> getTasksAssignedToUser(){
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        int id = principal.user().getUserId();

        List<Task> taskList =  taskRepository.findByAssignedToUser_UserId(id);
        return taskMapper.toDtoList(taskList);
    }

    public TaskDto updateTaskById(TaskDto t){

        Optional<Task> optTask = taskRepository.findById(t.getTaskId());
        if(optTask.isPresent()){
            Task task = optTask.get();
            Optional<User> updateUser = userRepository.findById(t.getAssignedToUser());
            task.setTitle(t.getTitle());
            task.setDescription(t.getDescription());
            task.setAssignedToUser(updateUser.orElseGet(task::getAssignedToUser));
            task.setPriority(t.getPriority());
            task.setStatus(t.getStatus());
            task.setDueDate(t.getDueDate());
            taskRepository.save(task);
            return t;
        }
        else{
            throw  new EntityNotFoundException("The task was not found");
        }
    }

    public void changeStatusOnDeletion(Task task , TaskStatus taskStatus){
        task.setStatus(taskStatus);
    }

    public boolean deleteTaskById(Integer id) {
        Optional<Task> optTask = taskRepository.findById(id);
        if(optTask.isPresent())
        {
            Task task = optTask.get();
            changeStatusOnDeletion(task,TaskStatus.BLOCKED);
            taskRepository.save(task);
            return true;
        }
        return false;
    }


}
