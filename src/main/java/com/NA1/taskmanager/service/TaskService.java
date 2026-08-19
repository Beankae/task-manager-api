package com.NA1.taskmanager.service;

import com.NA1.taskmanager.dto.TaskRequest;
import com.NA1.taskmanager.dto.TaskResponse;
import com.NA1.taskmanager.entity.Task;
import com.NA1.taskmanager.exception.TaskNotFoundException;
import com.NA1.taskmanager.mapper.TaskMapper;
import com.NA1.taskmanager.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Page<Task> searchTasksByTitle(String title, Pageable pageable){
        return taskRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Task> searchTasksByTitleAndCompletion(String title,
                                                      Boolean completed,
                                                      Pageable pageable) {
        return taskRepository.findByTitleContainingAndCompleted(title,
                completed, pageable);
    }

    public Page<Task> getTasksByCompletion(Boolean completed, Pageable pageable){
        return taskRepository.findByCompleted(completed, pageable);
    }


    //Get all tasks
    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);

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

    public Page<TaskResponse> getTaskByCompletionStatus(boolean status, Pageable pageable){

        final Page<Task> completedTasks = taskRepository.findByCompleted(status, pageable);
        return completedTasks.map(taskMapper::toResponse);
    }

    public List<TaskResponse> searchTasksByTitle(String title){
        return taskRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }
}
