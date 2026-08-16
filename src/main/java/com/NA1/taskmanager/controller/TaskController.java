package com.NA1.taskmanager.controller;

import com.NA1.taskmanager.entity.Task;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@RestController // @controller and @ResponseBody
@RequestMapping("/api/v1/tasks") // Base path for all endpoints localhost:8080/api/v1/tasks/

public class TaskController {

    // Temporary in-memory task list
    private List<Task> tasks = new ArrayList<>();
    private Long nextId = 1L;

    //Returning ALl tasks
    @GetMapping
    public List<Task> getAllTasks(){
        return tasks;
    }

    //Returning Tasks by ID
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id){
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);

    }

    //Creating each task
    @PostMapping
    public Task createTask(@RequestBody Task task){
        task.setId(nextId++);
        task.setCreatedAt(LocalDateTime.now());
        task.setCompleted(false);
        tasks.add(task);

        return task;

    }

    //Updating Task by ID
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask){
        for(int i = 0; i < tasks.size(); i++){
            Task task = tasks.get(i);
            if(task.getId().equals(id)){
                updatedTask.setId(id);
                updatedTask.setCreatedAt(task.getCreatedAt());
                tasks.set(i, updatedTask);
                return updatedTask;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        tasks.removeIf(task -> task.getId().equals(id));
    }


}
