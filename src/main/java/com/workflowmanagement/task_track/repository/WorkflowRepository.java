package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.dto.workflow.ResponseGetWorkflowByIDDTO;
import com.workflowmanagement.task_track.model.workflow.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Integer> {

    public ResponseGetWorkflowByIDDTO findByWorkflowId(Integer workflowId);
}
