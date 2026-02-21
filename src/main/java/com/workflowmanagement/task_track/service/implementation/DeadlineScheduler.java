package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.enums.notificationEnum.NotificationType;
import com.workflowmanagement.task_track.event.WorkflowEvent;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.SubTask;

import com.workflowmanagement.task_track.repository.SubTaskRepository;
import com.workflowmanagement.task_track.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeadlineScheduler {

    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final ApplicationEventPublisher publisher;

    // runs every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void checkDeadlines() {

        LocalDateTime now = LocalDateTime.now();

        checkTaskDeadlines(now);
        checkSubTaskDeadlines(now);
    }

    private void checkTaskDeadlines(LocalDateTime now) {

        List<Task> tasks = taskRepository.findAll();

        for (Task task : tasks) {

            if (task.getDueDate() == null) continue;

            long minutes = ChronoUnit.MINUTES.between(now, task.getDueDate());
            Integer userId = task.getAssignedToUser().getUserId();

            // 📅 1 DAY REMINDER
            if (minutes <= 1440 && minutes > 1435 && !task.getNotified1Day()) {

                publisher.publishEvent(new WorkflowEvent(
                        userId,
                        NotificationType.DEADLINE_ALERT,
                        "Task due tomorrow: " + task.getTitle()
                ));

                task.setNotified1Day(true);
            }

            // ⏰ 1 HOUR REMINDER
            if (minutes <= 60 && minutes > 55 && !task.getNotified1Hour()) {

                publisher.publishEvent(new WorkflowEvent(
                        userId,
                        NotificationType.DEADLINE_ALERT,
                        "Task due in 1 hour: " + task.getTitle()
                ));

                task.setNotified1Hour(true);
            }

            // 🚨 OVERDUE
            if (minutes < 0 && !task.getNotifiedOverdue()) {

                publisher.publishEvent(new WorkflowEvent(
                        userId,
                        NotificationType.DEADLINE_ALERT,
                        "Task overdue: " + task.getTitle()
                ));

                task.setNotifiedOverdue(true);
            }
        }

        taskRepository.saveAll(tasks);
    }

    private void checkSubTaskDeadlines(LocalDateTime now) {

        List<SubTask> list = subTaskRepository.findAll();

        for (SubTask st : list) {

            if (st.getDueDate() == null) continue;

            long minutes = ChronoUnit.MINUTES.between(now, st.getDueDate());
            Integer userId = st.getAssignedToUserId().getUserId();

            if (minutes <= 1440 && minutes > 1435 && !st.getNotified1Day()) {

                publisher.publishEvent(new WorkflowEvent(
                        userId,
                        NotificationType.DEADLINE_ALERT,
                        "Subtask due tomorrow: " + st.getTitle()
                ));

                st.setNotified1Day(true);
            }

            if (minutes <= 60 && minutes > 55 && !st.getNotified1Hour()) {

                publisher.publishEvent(new WorkflowEvent(
                        userId,
                        NotificationType.DEADLINE_ALERT,
                        "Subtask due in 1 hour: " + st.getTitle()
                ));

                st.setNotified1Hour(true);
            }

            if (minutes < 0 && !st.getNotifiedOverdue()) {

                publisher.publishEvent(new WorkflowEvent(
                        userId,
                        NotificationType.DEADLINE_ALERT,
                        "Subtask overdue: " + st.getTitle()
                ));

                st.setNotifiedOverdue(true);
            }
        }

        subTaskRepository.saveAll(list);
    }
}
