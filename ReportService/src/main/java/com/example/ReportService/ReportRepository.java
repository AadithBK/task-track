package com.example.ReportService;

import com.example.ReportService.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByUserId(Integer userId);
    List<Report> findByDepartmentName(String departmentName);
}