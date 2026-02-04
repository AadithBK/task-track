package com.workflowmanagement.task_track.dto.workflow.helper;

import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class HelperUserModel {
    private Integer userId;
    private String userName;
    private String email;
    private Department department;
}


