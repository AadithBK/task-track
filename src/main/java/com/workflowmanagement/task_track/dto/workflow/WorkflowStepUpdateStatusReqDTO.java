package com.workflowmanagement.task_track.dto.workflow;

import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class WorkflowStepUpdateStatusReqDTO {
    @NotNull
    private WorkflowStepStatus stepStatus;
    private Integer taskId;
    private  String message;

    @AssertTrue(message = "taskId and message are required when status is REJECTED")
    public boolean isValidRejectedState() {
        if(WorkflowStepStatus.REJECTED.equals(stepStatus)) return  taskId != null && !message.isBlank();
        return  true;
    }

}
