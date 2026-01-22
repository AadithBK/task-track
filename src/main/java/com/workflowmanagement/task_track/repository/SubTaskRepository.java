package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.model.SubTask;
import com.workflowmanagement.task_track.model.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, Integer> {
    List<SubTask> findAllByTaskId(Task task);

    Optional<SubTask> findBySubTaskIdAndTaskId(Integer subTaskId, Task taskId);

    @Modifying
    @Transactional
    void deleteAllByTaskId(Task findTask);
}
