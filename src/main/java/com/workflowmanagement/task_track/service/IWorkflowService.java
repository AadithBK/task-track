package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.dto.workflow.ResponseCreateNewWorkflowDTO;
import com.workflowmanagement.task_track.dto.workflow.ResponseGetWorkflowByIDDTO;
import com.workflowmanagement.task_track.dto.workflow.WorkflowStepUpdateStatusReqDTO;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import com.workflowmanagement.task_track.model.workflow.Workflow;

import java.util.List;

public interface IWorkflowService {

    public ResponseCreateNewWorkflowDTO createWorkflowFromTemplate(Integer workflowTemplateID, String workflowName);

    public  void  deleteWorkflow(Integer workflowId);

    public ResponseGetWorkflowByIDDTO getWorkflowById(Integer workflowId);

    public  List<Workflow> getWorkflowByDepartment(Department department);

    public void deleteWorkflowStep(Integer workflowId, Integer workflowStepId);

    void updateWorkflowStepStatus(Integer workflowStepId, WorkflowStepUpdateStatusReqDTO dto);
}
