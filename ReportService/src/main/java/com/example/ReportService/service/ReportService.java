package com.example.ReportService.service;

import com.example.ReportService.ReportRepository;
import com.example.ReportService.TaskClient;
import com.example.ReportService.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private TaskClient taskClient;

    @Autowired
    private ReportRepository reportRepository;

    // 1. Generate for User
    public Report generateUserReport(Integer userId) {
        long completed = taskClient.getCompletedCountByUser(userId);
        long total = taskClient.getTotalCountByUser(userId);

        return saveToDb("USER", userId, null, completed, total);
    }

    // 2. Generate for Department
    public Report generateDeptReport(String deptName) {
        long completed = taskClient.getCompletedCountByDept(deptName);
        long total = taskClient.getTotalCountByDept(deptName);

        return saveToDb("DEPARTMENT", null, deptName, completed, total);
    }

    private Report saveToDb(String scope, Integer userId, String dept, long comp, long total) {
        double rate = (total > 0) ? ((double) comp / total) * 100 : 0.0;

        Report report = Report.builder()
                .scope(scope)
                .userId(userId)
                .departmentName(dept)
                .tasksCompleted((int) comp)
                .pendingTasks((int) (total - comp))
                .completionRate(rate)
                .build();

        return reportRepository.save(report);
    }
}