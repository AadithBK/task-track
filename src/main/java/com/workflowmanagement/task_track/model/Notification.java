package com.workflowmanagement.task_track.model;

import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Setter
    private  Integer userId;
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length=50)
    private NotificationType type;

    @Setter
    private String message;
    @Setter
    private String status;
    private LocalDateTime createdDate;

    public Notification() {
        this.status = "UNREAD";
        this.createdDate = LocalDateTime.now();
    }


}