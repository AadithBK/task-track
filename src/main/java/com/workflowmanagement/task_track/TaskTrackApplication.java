package com.workflowmanagement.task_track;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class TaskTrackApplication {

    public static void main(String[] args) {
        // 1. Load .env file
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // 2. Set the variables into System properties so Spring can find them
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        // 3. Start Spring
        SpringApplication.run(TaskTrackApplication.class, args);
    }
}