package com.workflowmanagement.task_track.dto.subTask;

import com.workflowmanagement.task_track.enums.subTaskEnum.SubTaskStatusEnum;

public record SubTaskPatchReqDTO(SubTaskStatusEnum status) {
}
