package com.workflowmanagement.task_track.model.workflow.template;


import com.workflowmanagement.task_track.model.UserDataAudit;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder(toBuilder = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"workflow_template_step_id", "workflow_template_step_name"}),
                @UniqueConstraint(columnNames = {"workflow_template_step_id", "workflow_template_step_position"})
        }
)
public class WorkflowTemplate  extends UserDataAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workflowTemplateId;
    @Column(nullable = false)
    @NotBlank(message = "Template Name is required")
    private String workflowTemplateName;

    @Column(nullable = false)
    private boolean status = true;

@OneToMany(mappedBy = "workflowTemplate", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<WorkflowTemplateStep> workflowSteps;
}
