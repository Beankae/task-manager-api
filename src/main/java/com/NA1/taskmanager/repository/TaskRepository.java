package com.NA1.taskmanager.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.NA1.taskmanager.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
