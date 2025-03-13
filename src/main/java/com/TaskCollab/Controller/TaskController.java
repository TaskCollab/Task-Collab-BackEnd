package com.TaskCollab.Controller;

import com.TaskCollab.dto.TaskDTO;
import com.TaskCollab.Entity.TaskInterface;
import com.TaskCollab.Service.TaskService;
import com.TaskCollab.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtProperties jwtProperties;

    // POST request to retrieve a task by ID
    @PostMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        TaskInterface task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(convertToDTO(task)); // Convert TaskInterface to TaskDTO
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT request to update a task by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        TaskInterface updatedTask = taskService.updateTask(id, taskDTO);
        if (updatedTask != null) {
            return ResponseEntity.ok(convertToDTO(updatedTask)); // Convert TaskInterface to TaskDTO
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST request to create a new task
    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskInterface createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdTask)); // Convert TaskInterface to TaskDTO
    }

    // DELETE request to delete a task by ID
    @PostMapping("/delete/{task_Id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long task_Id) {
        boolean deleted = taskService.deleteTask(task_Id);
        if (deleted) {
            return ResponseEntity.ok("Task deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskDTO>> getMyTasks(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String secret = jwtProperties.getSecret();
        String username = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        List<TaskInterface> userTasks = taskService.getTasksByUsername(username);
        List<TaskDTO> taskDTOs = userTasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskDTOs);
    }

    // Helper method to convert TaskInterface to TaskDTO
    private TaskDTO convertToDTO(TaskInterface task) {
        TaskDTO dto = new TaskDTO();
        dto.setTask_Id(task.getTask_Id());
        dto.setTask_Title(task.getTask_Title());
        dto.setDescription(task.getDescription());
        dto.setAssigned_To(task.getAssigned_To());
        dto.setStatus(task.getStatus());
        dto.setDeadline(task.getDeadline());
        return dto;
    }
}