package com.workflowmanagement.task_track.model.workflow.template;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
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
public class WorkflowTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workflowTemplateId;
    @Column(nullable = false)
    @NotBlank(message = "Template Name is required")
    private String workflowTemplateName;
    @Column(nullable = false)
    @Min(value = 1, message = "Position must be at least 1")
    private int workflowTemplateSteps;

@OneToMany(mappedBy = "workflowTemplate", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<WorkflowTemplateStep> workflowSteps;
}
