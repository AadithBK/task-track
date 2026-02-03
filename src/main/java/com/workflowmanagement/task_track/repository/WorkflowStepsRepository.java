package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.model.workflow.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowStepsRepository extends JpaRepository<WorkflowStep, Integer> {
}
