package com.example.ReportService;

import com.example.ReportService.model.Report;
import com.example.ReportService.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ReportService.ReportController;


@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // POST: http://localhost:8083/api/reports/user/5
    @PostMapping("/user/{userId}")
    public ResponseEntity<Report> createUserReport(@PathVariable Integer userId) {
        return ResponseEntity.ok(reportService.generateUserReport(userId));
    }

    // POST: http://localhost:8083/api/reports/department/IT
    @PostMapping("/department/{deptName}")
    public ResponseEntity<Report> createDeptReport(@PathVariable String deptName) {
        return ResponseEntity.ok(reportService.generateDeptReport(deptName));
    }
}