package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.workflowmanagement.task_track.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer>{
    List<Task> findByCreatedByUser_UserId(Integer userId);
    List<Task> findByAssignedToUser_UserId(Integer userId);

    long countByAssignedToUser_UserIdAndStatus(Integer userId, TaskStatus status);

    // Count all tasks assigned to a user to calculate the rate
    long countByAssignedToUser_UserId(Integer userId);
    // Count completed tasks for all users in a specific department

    // Count by Department (Nested: Task -> User -> Department)
    // JPA maps this to: assignedToUser.department
    long countByAssignedToUser_Department(Department department);

    long countByAssignedToUser_DepartmentAndStatus(Department department, TaskStatus status);


}