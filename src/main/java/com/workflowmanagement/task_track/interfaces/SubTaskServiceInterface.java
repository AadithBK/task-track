package com.workflowmanagement.task_track.interfaces;

import com.workflowmanagement.task_track.dto.subTask.SubTaskCreateRequestDTO;
import com.workflowmanagement.task_track.dto.subTask.SubTaskUpdateReqDTO;
import com.workflowmanagement.task_track.model.SubTask;

import java.util.List;


public interface SubTaskServiceInterface {
    public SubTask save(SubTaskCreateRequestDTO subTaskCreateRequestDTOObject);

    //GET all tasks for a task
    public List<SubTask> getAllSubTasks( Integer taskId);
//
    //Delete a subtask
    public void deleteSubTask( Integer taskId, Integer subTaskId);

    //Put(update) a subtask
    public SubTask updateSubTask( SubTaskUpdateReqDTO subTaskyUpdateReqDTO, Integer taskId, Integer subTaskId);

    public  void deleteAllSubTask( Integer taskId);


}
