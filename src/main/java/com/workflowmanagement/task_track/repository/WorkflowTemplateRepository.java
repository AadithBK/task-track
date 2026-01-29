package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowTemplateRepository extends JpaRepository<WorkflowTemplate, Integer> {
    Optional<WorkflowTemplate> findByWorkflowTemplateIdAndStatusTrue(Integer templateId);

    List<WorkflowTemplate> findAllByStatusTrue();
}
