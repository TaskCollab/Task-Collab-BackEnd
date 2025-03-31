package com.TaskCollab.Controller;

import com.TaskCollab.dto.ActiveUserDTO;
import com.TaskCollab.dto.TaskDTO;
import com.TaskCollab.dto.TaskStatisticsDTO;
import com.TaskCollab.Entity.TaskInterface;
import com.TaskCollab.Service.FetchDataService;
import com.TaskCollab.Service.SearchService;
import com.TaskCollab.Service.TaskService;
import com.TaskCollab.config.JwtProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired 
    private ModelMapper modelMapper;

    @Autowired
    private FetchDataService fetchDataService;

    @PostMapping("/search")
    public ResponseEntity<List<TaskDTO>> searchTasks(@RequestBody TaskDTO searchCriteria) {
        List<TaskDTO> tasks = searchService.searchTasks(
                searchCriteria.getTaskTitle(),
                searchCriteria.getDescription(),
                searchCriteria.getAssignedTo(),
                searchCriteria.getStatus(),
                searchCriteria.getDeadline() != null ? searchCriteria.getDeadline().toString() : null
        ).stream()
         .map(task -> modelMapper.map(task, TaskDTO.class))
         .collect(Collectors.toList());

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        TaskInterface task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(modelMapper.map(task, TaskDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        TaskInterface updatedTask = taskService.updateTask(id, taskDTO);
        if (updatedTask != null) {
            return ResponseEntity.ok(modelMapper.map(updatedTask, TaskDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskInterface createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createdTask, TaskDTO.class));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
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
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskDTOs);
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<TaskStatisticsDTO>> getTaskStatistics() {
        List<TaskStatisticsDTO> taskStatistics = fetchDataService.getTaskStatistics();
        return ResponseEntity.ok(taskStatistics);
    }

    @GetMapping("/top5/{filterType}")
    public ResponseEntity<List<ActiveUserDTO>> getTop5UserWithMostTask(@PathVariable String filterType){
        List<ActiveUserDTO> activeUser = fetchDataService.getTop5UsersWithMostTasks(filterType); 
        return ResponseEntity.ok(activeUser);
    }
    @PutMapping("/lock/{id}")
public ResponseEntity<String> toggleLockStatus(
        @PathVariable Long id,
        @RequestParam boolean lock,
        HttpServletRequest request) {
    try {
        // Extract username from JWT token
        String token = request.getHeader("Authorization").substring(7);
        String secret = jwtProperties.getSecret();
        String username = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        // Check if user has ADMIN role (from authorities claim)
        List<String> roles = (List<String>) Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", List.class);

        if (!roles.contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can lock or unlock tasks.");
        }

        TaskInterface task = taskService.getTaskById(id);
        if (task == null) return ResponseEntity.notFound().build();

        TaskDTO dto = convertToDTO(task);
        dto.setLocked(lock);
        taskService.updateTask(id, dto);

        return ResponseEntity.ok("Task has been " + (lock ? "locked" : "unlocked") + " successfully.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
    }
}




    // Helper method to convert TaskInterface to TaskDTO
    private TaskDTO convertToDTO(TaskInterface task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getTask_Id());
        dto.setTaskTitle(task.getTask_Title());
        dto.setDescription(task.getDescription());
        dto.setAssignedTo(task.getAssigned_To());
        dto.setStatus(task.getStatus());
        dto.setDeadline(task.getDeadline());
        return dto;
    }
}