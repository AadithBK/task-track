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


        // Count tasks for a department by status
        // Use the Department Enum type instead of String
        @Query("SELECT COUNT(t) FROM Task t JOIN t.assignedToUser u WHERE u.department = :dept AND t.status = :status")
        long countByDepartmentAndStatus(@Param("dept") Department dept, @Param("status") TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t JOIN t.assignedToUser u WHERE u.department = :dept")
    long countTotalByDepartment(@Param("dept") Department dept);


}