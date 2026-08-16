package com.NA1.taskmanager.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.NA1.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    //Method Name Query
    // Select * FROM tasks WHERE completed = :completed
    List<Task> findByCompleted(boolean completed);

    List<Task> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT t FROM Task t WHERE t.completed = :completed")
    List<Task> findTasksByCompletionStatus(@Param("completed") boolean completed);
}
