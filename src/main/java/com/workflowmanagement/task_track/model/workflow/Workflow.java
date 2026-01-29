package com.workflowmanagement.task_track.model.workflow;


import com.workflowmanagement.task_track.enums.workspace.WorkflowStatus;
import com.workflowmanagement.task_track.model.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workflowId;


    @Column(nullable = false)
    private String workflowName;

    private Integer currentStep;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkflowStatus status;

@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
@JoinColumn(name = "fk_workflow_id")
    private List<WorkflowStep> workflowSteps = new ArrayList<>();


}
