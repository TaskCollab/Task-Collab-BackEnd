package com.TaskCollab.Controller;

import com.TaskCollab.Service.TaskService;
import com.TaskCollab.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // 2. Get a single task by ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return (task != null)
                ? ResponseEntity.ok(task)
                : ResponseEntity.notFound().build();
    }

    // 3. Create a new task
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // 4. Update an existing task
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return (updatedTask != null)
                ? ResponseEntity.ok(updatedTask)
                : ResponseEntity.notFound().build();
    }

    // 5. Delete a task by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.ok("Task deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. (Optional) Get tasks assigned to the current user
    //    Use this if you have user-based logic to identify "my tasks."
    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskDTO>> getMyTasks() {
        // This assumes you have logic to retrieve the currently authenticated user’s username
        // e.g., from Spring Security’s SecurityContextHolder, then do:
        // String currentUsername = ...
        // List<TaskDTO> myTasks = taskService.getTasksAssignedTo(currentUsername);
        // return ResponseEntity.ok(myTasks);

        return ResponseEntity.ok().build(); // Remove or implement properly if needed
    }
}
