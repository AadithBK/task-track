package com.workflowmanagement.task_track.model.workflow.template;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import org.hibernate.metamodel.mapping.ForeignKeyDescriptor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"workflow_id", "workflow_template_step_name"}),
                @UniqueConstraint(columnNames = {"workflow_id", "workflow_template_step_position"})
        }
)
public class WorkflowTemplateStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workflowTemplateStepId;

    @Column(nullable = false)
    @NotBlank(message = "Template Name is required")
    private String workflowTemplateStepName;

    @Column(nullable = false)
    @Min(value = 1, message = "Position must be at least 1" )
    private int workflowTemplateStepPosition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="workflow_id")
    private WorkflowTemplate workflowTemplate;
}
