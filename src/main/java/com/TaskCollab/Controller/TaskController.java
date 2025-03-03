package com.TaskCollab.Controller;

import com.TaskCollab.dto.TaskDTO;
import com.TaskCollab.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return (updatedTask != null) ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.ok("Task deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
