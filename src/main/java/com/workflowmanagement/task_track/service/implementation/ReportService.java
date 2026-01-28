package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.taskenum.TaskStatus;
import com.workflowmanagement.task_track.model.Report;
import com.workflowmanagement.task_track.repository.ReportRepository;
import com.workflowmanagement.task_track.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ReportRepository reportRepository;

    public Report generateUserReport(Integer userId) {
        // 1. Get raw data from Task Module
        long completed = taskRepository.countByAssignedToUser_UserIdAndStatus(userId, TaskStatus.COMPLETED);
        long total = taskRepository.countByAssignedToUser_UserId(userId);
        long pending = total - completed;

        // 2. Calculate completion rate (Productivity Metric)
        double rate = (total > 0) ? ((double) completed / total) * 100 : 0.0;

        // 3. Create and save the report
        Report report = Report.builder()
                .userId(userId)
                .tasksCompleted((int) completed)
                .pendingTasks((int) pending)
                .completionRate(rate)
                .build();

        return reportRepository.save(report);
    }

    public Report generateDepartmentReport(String deptName) {
        // Convert String input to Enum
        Department deptEnum = Department.valueOf(deptName);

        long completed = taskRepository.countByDepartmentAndStatus(deptEnum, TaskStatus.COMPLETED);
        long total = taskRepository.countTotalByDepartment(deptEnum);
        long pending = total - completed;

        double rate = (total > 0) ? ((double) completed / total) * 100 : 0.0;

        Report report = Report.builder()
                .scope("DEPARTMENT")
                .departmentName(deptName)
                .tasksCompleted((int) completed)
                .pendingTasks((int) pending)
                .completionRate(rate)
                .build();

        Report savedReport = reportRepository.save(report);

        return savedReport;
    }
}