package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.dto.workflow.ResponseCreateNewWorkflowDTO;
import com.workflowmanagement.task_track.dto.workflow.ResponseGetWorkflowByIDDTO;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.model.workflow.Workflow;

import java.util.List;

public interface IWorkflowService {

    public ResponseCreateNewWorkflowDTO createWorkflowFromTemplate(Integer workflowTemplateID);

    public  void  deleteWorkflow(Integer workflowId);

    public ResponseGetWorkflowByIDDTO getWorkflowById(Integer workflowId);

    public  List<Workflow> getWorkflowByDepartment(Department department);

}
