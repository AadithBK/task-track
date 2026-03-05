package com.workflowmanagement.task_track.model.workflow;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.workflowmanagement.task_track.enums.workspace.WorkflowStepStatus;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder()
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workflowStepId;

    @Column(nullable = false)
    private String stepName;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name =  "fk_workflow_assigned_to_user")
//    private User assignedToUserId;

    @OneToMany(mappedBy = "workflowStep", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Task> task = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private WorkflowStepStatus status;

    private Integer step;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_workflow_id") // The physical column remains here
    private Workflow workflow;

}
