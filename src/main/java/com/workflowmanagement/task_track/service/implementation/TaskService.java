package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.dto.ResponseTaskCreateDto;
import com.workflowmanagement.task_track.dto.TaskCreateDto;
import com.workflowmanagement.task_track.dto.TaskDto;
import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.Role;
import com.workflowmanagement.task_track.enums.subTaskEnum.SubTaskStatusEnum;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;

import com.workflowmanagement.task_track.exceptions.InvalidCredentialsException;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStatus;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import com.workflowmanagement.task_track.mapper.TaskMapper;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;

import com.workflowmanagement.task_track.model.workflow.Workflow;
import com.workflowmanagement.task_track.model.workflow.WorkflowStep;

import com.workflowmanagement.task_track.repository.*;

import com.workflowmanagement.task_track.service.ITaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import org.springframework.context.ApplicationEventPublisher;
import com.workflowmanagement.task_track.event.WorkflowEvent;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor

public class TaskService implements ITaskService {
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TaskRepository taskRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final WorkflowStepsRepository workflowStepsRepository;



    public TaskDto createTask(TaskCreateDto task){
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        Integer user = principal.user().getUserId();
        Optional<User> optAssignedUser = userService.getUserObjById(task.getAssignedToUserId());
        Optional<User> optCreatedUser = userService.getUserObjById(user);
        WorkflowStep workflowStep = workflowStepsRepository.findById(task.getWorkflowStepId()).orElseThrow(() -> new EntityNotFoundException("Workflow Step with ID " + task.getWorkflowStepId() + " does not exist"));

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
                    .workflowStep(workflowStep)
                    .build();
            assignedToUser.getAssignedTasks().add(createdTask);
            createdByUser.getCreatedTasks().add(createdTask);

            Workflow workflow = workflowStep.getWorkflow();
            // Set workflow Status Level - 1


            workflow.setStatus(WorkflowStatus.INPROGRESS);
            workflowStep.setStatus(WorkflowStepStatus.PENDING);
            //Set status for workflow step level - 2
            workflowStepsRepository.save(workflowStep);
            taskRepository.save(createdTask);
            eventPublisher.publishEvent(new WorkflowEvent(assignedToUser.getUserId(), NotificationType.TASK_ASSIGNED, "New task assigned: "+ createdTask.getTitle()));
            return taskMapper.toDto(createdTask);
        }
        else {
            throw new EntityNotFoundException("The given user id does not match with the records");
        }

    }

    public List<TaskDto> getAllTasks(){
        List<Task> taskList =  taskRepository.findAll();
        System.out.println(taskList.get(0).getSubTasks().size());
        return taskMapper.toDtoList(taskList);
    }

    @Override
    public List<TaskDto> getTasksByCreatorId() {
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        int id = principal.user().getUserId();
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

    @Override
    public TaskDto getTaskById(int id) {
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        int userId = principal.user().getUserId();
        Optional<Task> optTask = taskRepository.findById(id);
        Task task = optTask.get();
        if(task.getAssignedToUser().getUserId().equals(userId) || Role.MANAGER.equals(principal.user().getRole())){
            return taskMapper.toDto(task);
        }
        else{
            throw new InvalidCredentialsException("You do not have access to this task");
        }
    }

    public TaskDto updateTaskById(TaskDto t){

        Optional<Task> optTask = taskRepository.findById(t.getTaskId());
        if(optTask.isPresent()){

            //  Fetch task
            Task task = optTask.get();

            //  Capture OLD values
            TaskStatus oldStatus = task.getStatus();
            Integer oldAssigneeId = task.getAssignedToUser().getUserId();

            //  Update task
            Optional<User> updateUser = userRepository.findById(t.getAssignedToUser());
            task.setTitle(t.getTitle());
            task.setDescription(t.getDescription());
            task.setAssignedToUser(updateUser.orElseGet(task::getAssignedToUser));
            task.setPriority(t.getPriority());

            task.setStatus(t.getStatus());
            task.setDueDate(t.getDueDate());

            //  Save
            taskRepository.save(task);

            // Compare & trigger notifications

            // Status changed
            if (oldStatus != task.getStatus()) {
                eventPublisher.publishEvent(
                        new WorkflowEvent(
                                task.getCreatedByUser().getUserId(),
                                NotificationType.TASK_STATUS_UPDATE,
                                "Task status updated to " + task.getStatus() + ": " + task.getTitle()
                        )
                );
            }

            // Assignee changed
            Integer newAssigneeId = task.getAssignedToUser().getUserId();
            if (!oldAssigneeId.equals(newAssigneeId)) {
                eventPublisher.publishEvent(
                        new WorkflowEvent(
                                newAssigneeId,
                                NotificationType.TASK_ASSIGNED,
                                "Task reassigned to you: " + task.getTitle()
                        )
                );
            }

            return taskMapper.toDto(task);
        }
        else{
            throw new EntityNotFoundException("The task was not found");
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

    @Override
    @Transactional
    public TaskDto updateTaskStatus(Integer taskId, TaskStatus taskStatus) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("The task was not found"));
        if(task.getStatus().equals(TaskStatus.COMPLETED)) throw new ValidationException("Task has been completed");
      if(task.getStatus().equals(taskStatus)) throw new ValidationException("New state is same as old state");
      if(taskStatus.equals(TaskStatus.COMPLETED) && task.getSubTasks()
                .stream()
                .filter(subTask -> subTask.getStatus().equals(SubTaskStatusEnum.COMPLETED))
                .toArray().length != task.getSubTasks().toArray().length) throw new ValidationException("Some task are not completed");


      task.setStatus(taskStatus);

      if(task.getSubTasks().stream().allMatch(el -> el.getStatus().equals(SubTaskStatusEnum.COMPLETED))) {
          task.setStatus(TaskStatus.COMPLETED);
      } else if(task.getSubTasks().stream().anyMatch(el -> el.getStatus().equals(SubTaskStatusEnum.INPROGRESS))) {
          task.setStatus(TaskStatus.IN_PROGRESS);
      } else {
          task.setStatus(TaskStatus.OPEN);
      }
        eventPublisher.publishEvent(
                new WorkflowEvent(
                        task.getCreatedByUser().getUserId(),
                        NotificationType.TASK_STATUS_UPDATE,
                        "Task reassigned to you: " + task.getTitle()
                )
        );
        return  taskMapper.toDto(task);
    }


    public long getCompletedCountByUser(Integer userId) {
        // Note: Using the TaskStatus Enum instead of String "COMPLETED"
        return taskRepository.countByAssignedToUser_UserIdAndStatus(userId, TaskStatus.COMPLETED);
    }


    public long getTotalCountByUser(Integer userId) {
        return taskRepository.countByAssignedToUser_UserId(userId);
    }

    public long getCompletedCountByDept(String deptName) {
        // Convert the String from the URL to your Department Enum
        Department dept = Department.valueOf(deptName.toUpperCase());
        return taskRepository.countByAssignedToUser_DepartmentAndStatus(dept, TaskStatus.COMPLETED);
    }

    public long getTotalCountByDept(String deptName) {
        Department dept = Department.valueOf(deptName.toUpperCase());
        return taskRepository.countByAssignedToUser_Department(dept);
    }


}