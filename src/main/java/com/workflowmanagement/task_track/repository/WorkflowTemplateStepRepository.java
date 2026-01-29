package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WorkflowTemplateStepRepository extends JpaRepository<WorkflowTemplateStep, Integer> {

}
