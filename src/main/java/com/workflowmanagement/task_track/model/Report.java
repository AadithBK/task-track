package com.workflowmanagement.task_track.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportID;

    private Integer userId; // The specific user this report is for
    private String departmentName; // New field
    private String scope;

    private Integer tasksCompleted;
    private Integer pendingTasks;
    private Double completionRate; // (Completed / Total) * 100
    private LocalDateTime generatedDate;

    @PrePersist
    protected void onCreate() {
        this.generatedDate = LocalDateTime.now();
    }
}