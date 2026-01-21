package com.workflowmanagement.task_track.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.workflowmanagement.task_track.enums.taskenum.TaskPriority;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;

    @Column(nullable = false)
    @NotBlank(message = "Provide a title")
    private String title;

    @Column(nullable = true)
    private String description;

    @ManyToOne(
            fetch = FetchType.LAZY ,
            optional = true)
    @JoinColumn(
            name = "assigned_to_user_id" ,
            referencedColumnName = "userId",
            nullable = true,
            foreignKey = @ForeignKey(name = "fk_task_assigned_user"
            )
    )
    private User assignedToUser;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "created_by_user_id",
            referencedColumnName = "userId",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_task_created_by_user")
    )
    private User createdByUser ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Please select a priority")
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Please select a status")
    private TaskStatus status;

    @Column()
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant dueDate ;
}
