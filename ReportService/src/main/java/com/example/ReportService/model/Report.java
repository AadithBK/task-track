package com.example.ReportService.model;

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

    private String scope;          // "USER" or "DEPARTMENT"
    private Integer userId;       // UserID (if scope is USER)
    private String departmentName; // Dept Name (if scope is DEPARTMENT)

    private Integer tasksCompleted;
    private Integer pendingTasks;
    private Double completionRate;
    private LocalDateTime generatedDate;

    @PrePersist
    protected void onCreate() {
        this.generatedDate = LocalDateTime.now();
    }
}