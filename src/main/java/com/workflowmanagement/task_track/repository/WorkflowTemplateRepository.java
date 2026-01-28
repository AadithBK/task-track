package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowTemplateRepository extends JpaRepository<WorkflowTemplate, Integer> {

}
