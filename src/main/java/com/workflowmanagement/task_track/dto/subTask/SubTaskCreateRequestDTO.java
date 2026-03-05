package com.workflowmanagement.task_track.dto.subTask;


import com.workflowmanagement.task_track.enums.subTaskEnum.SubTaskStatusEnum;

public record SubTaskCreateRequestDTO(Integer taskId, String title, SubTaskStatusEnum status) {
}
