package com.workflowmanagement.task_track.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private Long taskId;   // Foreign key to Task

    @Column(nullable = false)
    private Integer userId;   // Foreign key to User

    @Column(nullable = false, length = 1000)
    private String text;


    private String authorName;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate ;

    // Getters and setters
}

