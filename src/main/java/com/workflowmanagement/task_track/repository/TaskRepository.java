package com.workflowmanagement.task_track.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.workflowmanagement.task_track.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer>{
    List<Task> findByCreatedByUser_UserId(Integer userId);
    List<Task> findByAssignedToUser_UserId(Integer userId);


}
