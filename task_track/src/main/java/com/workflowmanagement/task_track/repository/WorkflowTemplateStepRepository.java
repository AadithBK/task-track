package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowTemplateStepRepository extends JpaRepository<WorkflowTemplateStep, Integer> {

}
