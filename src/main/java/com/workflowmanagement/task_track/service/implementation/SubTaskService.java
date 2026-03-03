package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.dto.subTask.SubTaskCreateRequestDTO;
import com.workflowmanagement.task_track.dto.subTask.SubTaskPatchReqDTO;
import com.workflowmanagement.task_track.dto.subTask.SubTaskUpdateReqDTO;
import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import com.workflowmanagement.task_track.enums.subTaskEnum.SubTaskStatusEnum;
import com.workflowmanagement.task_track.event.WorkflowEvent;
import com.workflowmanagement.task_track.model.SubTask;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.repository.SubTaskRepository;
import com.workflowmanagement.task_track.repository.TaskRepository;
import com.workflowmanagement.task_track.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

@Service
public  class SubTaskService implements com.workflowmanagement.task_track.service.ISubTaskService {
    @Autowired
    private SubTaskRepository subTaskRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
@Autowired
private UserRepository userRepository;

    public SubTask save(SubTaskCreateRequestDTO subTaskDTOObject) {
        Task findParentTask = taskRepository.findById(subTaskDTOObject.taskId()).orElseThrow(() -> new EntityNotFoundException("Parent Task does not exists"));
//        User findAssignedToUser = userRepository.findById(subTaskDTOObject.assignedToUserId()).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        SubTask newSubTask = SubTask.builder()
                .taskId(findParentTask)
                .title(subTaskDTOObject.title())
                .status(
                        subTaskDTOObject.status() != null? SubTaskStatusEnum.valueOf(String.valueOf(subTaskDTOObject.status())): SubTaskStatusEnum.OPEN)
                .assignedToUserId(findParentTask.getAssignedToUser())
                .build();
//        System.out.println(newSubTask.getTaskId() + " " + newSubTask.getAssignedToUserId().getUserId() + " " + newSubTask);

        SubTask saved = subTaskRepository.save(newSubTask);
        eventPublisher.publishEvent(
                new WorkflowEvent(
                        saved.getAssignedToUserId().getUserId(),
                        NotificationType.TASK_ASSIGNED,
                        "New subtask assigned: " + saved.getTitle()
                )
        );
        return saved;



    }


    @Override
    public List<SubTask> getSubTaskById(Integer subtaskId) {
        Task findParentTask = taskRepository.findById(subtaskId).orElseThrow(() -> new EntityNotFoundException("Parent Task does not exists"));

        return subTaskRepository.findAllByTaskId(findParentTask);
    }


    @Override
    @Transactional
    public SubTask updateSubTask(SubTaskUpdateReqDTO subTaskUpdateReqDTO, Integer taskId, Integer subTaskId) {
//        User mentionedUser = userRepository.findById(subTaskDTOObject.assignedToUserId()).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        int id = principal.user().getUserId();

        User mentionedUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        Task findTask = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Parent Task does not exists"));
        if(mentionedUser.getUserId().equals(findTask.getCreatedByUser().getUserId())) {
            Optional<SubTask> subTask = subTaskRepository.findBySubTaskIdAndTaskId(subTaskId, findTask);
            subTask.orElseThrow(() -> new EntityNotFoundException("Sub Task not found"));
            SubTask st = subTask.get();
            Integer oldAssigneeId = st.getAssignedToUserId().getUserId();
            SubTaskStatusEnum oldStatus = st.getStatus();
            Boolean isUpdated = false;
                if((st.getAssignedToUserId().getUserId() != subTaskUpdateReqDTO.assignedToUserId()) && subTaskUpdateReqDTO.assignedToUserId() != null ) {
                    User assignToNewUser =  userRepository.findById(subTaskUpdateReqDTO.assignedToUserId()).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
                    st.setAssignedToUserId(assignToNewUser);
                    isUpdated = true;
                }

                if((st.getTitle() != subTaskUpdateReqDTO.title()) && subTaskUpdateReqDTO.title() != null) {
                    st.setTitle(subTaskUpdateReqDTO.title());
                    isUpdated = true;
                }
              if(st.getStatus() != subTaskUpdateReqDTO.status() && subTaskUpdateReqDTO.status() != null) {
                  st.setStatus(subTaskUpdateReqDTO.status());
                  isUpdated = true;
              }



// REASSIGN NOTIFICATION
            if (!oldAssigneeId.equals(st.getAssignedToUserId().getUserId())) {
                eventPublisher.publishEvent(
                        new WorkflowEvent(
                                st.getAssignedToUserId().getUserId(),
                                NotificationType.TASK_ASSIGNED,
                                "Subtask reassigned to you: " + st.getTitle()
                        )
                );
            }

//  STATUS CHANGE NOTIFICATION
            if (oldStatus != st.getStatus()) {
                eventPublisher.publishEvent(
                        new WorkflowEvent(
                                findTask.getAssignedToUser().getUserId(),
                                NotificationType.TASK_STATUS_UPDATE,
                                "Subtask status updated to " + st.getStatus() + ": " + st.getTitle()
                        )
                );
            }

            return st;

        } else {
            throw new ValidationException("Task is created by another user");
        }




    }

    @Override
    public void deleteSubTask(Integer taskId, Integer subTaskId) {
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        int id = principal.user().getUserId();

        User mentionedUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        Task findTask = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Parent Task does not exists"));
        if(mentionedUser.getUserId().equals(findTask.getCreatedByUser().getUserId())) {
            Optional<SubTask> subTask = subTaskRepository.findBySubTaskIdAndTaskId(subTaskId, findTask);
            subTask.orElseThrow(() -> new EntityNotFoundException("Sub Task not found"));
            subTaskRepository.deleteById(subTaskId);
        } else {
            throw new ValidationException("Task is created by another user");
        }
    }

    @Override
    public void deleteAllSubTask(Integer taskId) {
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        int id = principal.user().getUserId();

        User mentionedUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        Task findTask = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Parent Task does not exists"));
        if(mentionedUser.getUserId().equals(findTask.getCreatedByUser().getUserId())) {

            subTaskRepository.deleteAllByTaskId(findTask);
        } else {
            throw new ValidationException("Task is created by another user");
        }
    }

    @Override
    @Transactional
    public SubTask updateStatusForSubTask(Integer taskId, Integer subTaskId, SubTaskPatchReqDTO subTaskPatchReqDTO) {
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        int id = principal.user().getUserId();

        User mentionedUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        Task findTask = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Parent Task does not exists"));
        if(mentionedUser.getUserId().equals(findTask.getCreatedByUser().getUserId())) {
            Optional<SubTask> subTask = subTaskRepository.findBySubTaskIdAndTaskId(subTaskId, findTask);
            subTask.orElseThrow(() -> new EntityNotFoundException("Sub Task not found"));
            SubTask st = subTask.get();
            SubTaskStatusEnum oldStatus = st.getStatus();
            st.setStatus(subTaskPatchReqDTO.status());

            if (oldStatus != subTaskPatchReqDTO.status()) {
                eventPublisher.publishEvent(
                        new WorkflowEvent(
                                findTask.getCreatedByUser().getUserId(),
                                NotificationType.TASK_STATUS_UPDATE,
                                "Subtask status updated to " + subTaskPatchReqDTO.status() + ": " + st.getTitle()
                        )
                );
            }

            return st;
        } else {
            throw new ValidationException("Task is created by another user");
        }

    }

    @Override
    @Transactional
    public void updateSubTaskStatus(Integer taskId, Integer subTaskId, SubTaskStatusEnum status) {

        SubTask subTask = subTaskRepository
                .findBySubTaskIdAndTaskId_TaskId(subTaskId, taskId)
                .orElseThrow(() -> new EntityNotFoundException("Sub Task not found"));

        SubTaskStatusEnum oldStatus = subTask.getStatus();
        subTask.setStatus(status);

        if (oldStatus != status) {
            eventPublisher.publishEvent(
                    new WorkflowEvent(
                            subTask.getTaskId().getAssignedToUser().getUserId(),
                            NotificationType.TASK_STATUS_UPDATE,
                            "Subtask status updated to " + status + ": " + subTask.getTitle()
                    )
            );
        }
    }


}
