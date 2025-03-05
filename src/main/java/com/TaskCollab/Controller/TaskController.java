package com.TaskCollab.Controller;

import com.TaskCollab.dto.TaskDTO;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import com.TaskCollab.Service.TaskService;
import com.TaskCollab.config.JwtUtils;
import com.TaskCollab.config.JwtProperties;

import java.net.http.HttpHeaders;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/task/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return (updatedTask != null) ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

   @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskDTO>> getMyTasks(HttpServletRequest request) { // Add HttpServletRequest
        String token = request.getHeader("Authorization").substring(7); // Extract token from Authorization header

        String secret = jwtProperties.getSecret();
        String username = Jwts.parserBuilder() // Use parserBuilder
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())) // Use getBytes()
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        System.out.println(username);
                
        List<TaskDTO> userTasks = taskService.getTasksByUsername(username);

        return ResponseEntity.ok(userTasks);
    }

}
