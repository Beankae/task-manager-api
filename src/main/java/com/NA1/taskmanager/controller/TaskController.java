package com.NA1.taskmanager.controller;

import com.NA1.taskmanager.entity.Task;
import com.NA1.taskmanager.service.TaskService;
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
    public List<Task> getAllTasks(){
        return taskService.getAllTasks();
    }

    //Returning Tasks by ID
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    //Creating each task
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task){
       Task savedTask = taskService.createTask(task);
       return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    //Updating Task by ID
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask){

        return taskService.updateTask(id, updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
                taskService.deleteTask(id);
                return ResponseEntity.ok().build();
    }

    @GetMapping("/completed/{status}")
    public List<Task> getTasksByCompletion(@PathVariable boolean status){
        return taskService.getTaskByCompletionStatus(status);
    }

    @GetMapping("/search")
    public List<Task> searchTasksByTitle(@RequestParam String title){
        return taskService.searchTasksByTitle(title);
    }


}
