package com.workflowmanagement.task_track.dto.workflow.helper;

import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Helper;

import java.util.List;

@Getter
@Setter
public class HelperTaskModel {
    private Integer taskId;
    private String title;
    private String description;
    private HelperUserModel assignedToUser;
    private HelperUserModel createdByUser;
    private List<HelperSubTaskModel> subTasks;
}

