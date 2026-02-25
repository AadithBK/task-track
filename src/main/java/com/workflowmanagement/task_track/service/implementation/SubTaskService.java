package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.dto.subTask.SubTaskCreateRequestDTO;
import com.workflowmanagement.task_track.dto.subTask.SubTaskPatchReqDTO;
import com.workflowmanagement.task_track.dto.subTask.SubTaskUpdateReqDTO;
import com.workflowmanagement.task_track.enums.subTaskEnum.SubTaskStatusEnum;
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

        return subTaskRepository.save(newSubTask);


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


                return  st;

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
            st.setStatus(subTaskPatchReqDTO.status());
            return  st;
        } else {
            throw new ValidationException("Task is created by another user");
        }

    }

    @Override
    @Transactional
    public void updateSubTaskStatus(Integer taskId, Integer subTaskId, SubTaskStatusEnum status) {
        SubTask subTask = subTaskRepository.findBySubTaskIdAndTaskId_TaskId(subTaskId, taskId).orElseThrow(() -> new EntityNotFoundException("Sub Task not found"));
     subTask.setStatus(status);

    }


}
