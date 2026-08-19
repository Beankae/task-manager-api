package com.NA1.taskmanager.mapper;

import com.NA1.taskmanager.dto.TaskRequest;
import com.NA1.taskmanager.dto.TaskResponse;
import com.NA1.taskmanager.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequest request){
        return Task.builder()
                .title(request.title())
                .description(request.description())
                .completed(request.completed() != null ? request.completed() : false)
                .build();
    }

    public TaskResponse toResponse(Task task){
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.getCompleted())
                .createdAt(task.getCreatedAt())
                .build();
    }

    public void updateEntityFromRequest(Task task, TaskRequest request){
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCompleted(request.completed());
    }
}
