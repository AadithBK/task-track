package com.workflowmanagement.task_track.dto.workflow.helper;

import com.workflowmanagement.task_track.enums.subTaskEnum.SubTaskStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@NotNull
@Data
public class HelperSubTaskModel {

   private Integer subTaskId;
   private SubTaskStatusEnum status;
   private String title;
   private HelperUserModel assignedToUserId;
}


