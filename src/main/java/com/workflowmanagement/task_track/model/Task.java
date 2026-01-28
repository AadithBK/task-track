package com.workflowmanagement.task_track.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.workflowmanagement.task_track.enums.taskenum.TaskPriority;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subTask")
//    @JoinColumn(name="task_id_fk", referencedColumnName = "taskId", nullable = false, foreignKey =  @ForeignKey(name = "fk_sub_task_task_id"))
//@OneToMany(mappedBy = "taskId")
    private Integer taskId;
//    @OneToMany(mappedBy = "taskId", fetch = FetchType.LAZY)
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    private List<SubTask> subTasks;

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


//    @OneToMany(mappedBy = "taskId")
//    @JsonManagedReference
//    private List<SubTask> subTask;
}
