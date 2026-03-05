package com.workflowmanagement.task_track.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workflowmanagement.task_track.enums.subTaskEnum.SubTaskStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SubTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subTaskId;

    private Boolean notified1Day = false;
    private Boolean notified1Hour = false;
    private Boolean notifiedOverdue = false;

    @ManyToOne(
            fetch = FetchType.LAZY

    )
    @JoinColumn(
            name="task_id",
            referencedColumnName = "taskId",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_sub_task_task_id")
    )
    @JsonIgnore
    private Task taskId;

    @Column(nullable = false)
    @NotBlank(message = "Subtask title cannot be null/blank")
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubTaskStatusEnum status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="assigned_to_user_id", nullable = false)
    @JsonIgnore
    private User assignedToUserId ;
}
