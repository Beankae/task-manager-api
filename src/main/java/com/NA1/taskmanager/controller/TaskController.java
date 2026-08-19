package com.NA1.taskmanager.controller;

import com.NA1.taskmanager.dto.TaskRequest;
import com.NA1.taskmanager.dto.TaskResponse;
import com.NA1.taskmanager.entity.Task;
import com.NA1.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;



@RestController // @controller and @ResponseBody
@RequestMapping("/api/v1/tasks") // Base path for all endpoints localhost:8080/api/v1/tasks/
public class TaskController {

    private final TaskService taskService;

    //Constructor Injection
    //@Autowired
    public TaskController(TaskService taskservice) {
        this.taskService = taskservice;
    }

    //Returning ALl tasks
    @GetMapping
    public List<TaskResponse> getAllTasks(){
        return taskService.getAllTasks();
    }

    //Returning Tasks by ID
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    //Creating each task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest task){
       TaskResponse savedTask = taskService.createTask(task);
       return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    //Updating Task by ID
    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest updatedTask){

        return taskService.updateTask(id, updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
                taskService.deleteTask(id);
                return ResponseEntity.ok().build();
    }

    @GetMapping("/completed/{status}")
    public List<TaskResponse> getTasksByCompletion(@PathVariable boolean status){
        return taskService.getTaskByCompletionStatus(status);
    }

    @GetMapping("/search")
    public List<TaskResponse> searchTasksByTitle(@RequestParam String title){
        return taskService.searchTasksByTitle(title);
    }


}
