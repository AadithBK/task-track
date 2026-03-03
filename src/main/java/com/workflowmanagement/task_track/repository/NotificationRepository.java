package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserId(Integer userId);
    long countByUserIdAndStatus(Integer userId, String status);
}