package com.NA1.taskmanager.service;

import com.NA1.taskmanager.dto.TaskRequest;
import com.NA1.taskmanager.dto.TaskResponse;
import com.NA1.taskmanager.entity.Task;
import com.NA1.taskmanager.exception.TaskNotFoundException;
import com.NA1.taskmanager.mapper.TaskMapper;
import com.NA1.taskmanager.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    //Get all tasks
    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    public TaskResponse getTaskById(Long id){
        Task retrievedTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponse(retrievedTask);
    }

    public TaskResponse createTask(TaskRequest task){
        Task entityTask = taskMapper.toEntity(task);
        Task savedTask = taskRepository.save(entityTask);
        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse updateTask(Long id, TaskRequest updatedTask){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskMapper.updateEntityFromRequest(task, updatedTask);
        taskRepository.save(task);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);

    }

    public List<TaskResponse> getTaskByCompletionStatus(boolean status){
        return taskRepository.findByCompleted(status)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    public List<TaskResponse> searchTasksByTitle(String title){
        return taskRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }
}
