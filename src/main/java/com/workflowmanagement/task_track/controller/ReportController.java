package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.model.Report;
import com.workflowmanagement.task_track.service.implementation.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate/{userId}")
    public ResponseEntity<Report> createReport(@PathVariable Integer userId) {
        return ResponseEntity.ok(reportService.generateUserReport(userId));
    }
    @PostMapping("/generate/department/{deptName}")
    public ResponseEntity<Report> createDepartmentReport(@PathVariable String deptName) {
        return ResponseEntity.ok(reportService.generateDepartmentReport(deptName));
    }
}