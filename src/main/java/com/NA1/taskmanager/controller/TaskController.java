package com.NA1.taskmanager.controller;

import com.NA1.taskmanager.dto.TaskRequest;
import com.NA1.taskmanager.dto.TaskResponse;
import com.NA1.taskmanager.entity.Task;
import com.NA1.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController // @controller and @ResponseBody
@RequestMapping("/api/v1/tasks") // Base path for all endpoints localhost:8080/api/v1/tasks/
public class TaskController {

    private final TaskService taskService;

    //Constructor Injection
    //@Autowired
    public TaskController(TaskService taskservice) {
        this.taskService = taskservice;
    }
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "Desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("ASC") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> taskPage;

        if (title != null && completed != null) {
            //  Filter by both
            taskPage = taskService.searchTasksByTitleAndCompletion(
                    title, completed, pageable
            );
        } else if (title != null) {
            // Filter by title only
            taskPage = taskService.searchTasksByTitle(title, pageable);
        } else if (completed != null) {
            // Filter by completion Only
            taskPage = taskService.getTasksByCompletion(completed, pageable);
        } else {
            taskPage = taskService.getAllTasks(pageable);
        }

        List<TaskResponse> tasks = taskPage.getContent()
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getCompleted(),
                        task.getCreatedAt()))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("tasks", tasks);
        response.put("currentPage", taskPage.getNumber());
        response.put("totalItems", taskPage.getTotalElements());
        response.put("totalPages", taskPage.getTotalPages());
        response.put("hasNext", taskPage.hasNext());
        response.put("hasPrevious", taskPage.hasPrevious());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTasks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "CreatedAt") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir){
        Sort sort = sortDir.equalsIgnoreCase("ASC") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> taskPage = taskService.getAllTasks(pageable);

        List<TaskResponse> tasks = taskPage.getContent()
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getCompleted(),
                        task.getCreatedAt()))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("tasks", tasks);
        response.put("currentPage", taskPage.getNumber());
        response.put("totalItems", taskPage.getTotalElements());
        response.put("totalPages", taskPage.getTotalPages());
        response.put("hasNext", taskPage.hasNext());
        response.put("hasPrevious", taskPage.hasPrevious());

        return new ResponseEntity<>(response, HttpStatus.OK);
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

    @GetMapping("/search-by-title")
    public List<TaskResponse> searchTasksByTitle(@RequestParam String title){
        return taskService.searchTasksByTitle(title);
    }


}
