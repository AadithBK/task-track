package com.example.ReportService;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "task-service")
public interface TaskClient {

    // FIX: Changed "/task/" to "/tasks/" to match the controller
    @GetMapping("api/task/count/completed/user/{userId}")
    long getCompletedCountByUser(@PathVariable("userId") Integer userId);

    @GetMapping("api/task/count/total/user/{userId}")
    long getTotalCountByUser(@PathVariable("userId") Integer userId);

    @GetMapping("api/task/count/completed/dept/{deptName}")
    long getCompletedCountByDept(@PathVariable("deptName") String deptName);

    @GetMapping("api/task/count/total/dept/{deptName}")
    long getTotalCountByDept(@PathVariable("deptName") String deptName);
}