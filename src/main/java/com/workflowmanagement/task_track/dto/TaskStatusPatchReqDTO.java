package com.workflowmanagement.task_track.dto;

import com.workflowmanagement.task_track.enums.subTaskEnum.SubTaskStatusEnum;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;

public record TaskStatusPatchReqDTO(TaskStatus status) {
}
