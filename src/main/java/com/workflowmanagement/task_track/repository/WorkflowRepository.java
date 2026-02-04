package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.dto.workflow.ResponseGetWorkflowByIDDTO;
import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.model.workflow.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Integer> {




    public Optional<List<Workflow>> findByCreatedBy_Department( Department department);
}