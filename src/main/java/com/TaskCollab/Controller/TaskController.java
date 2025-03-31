package com.TaskCollab.Controller;

import com.TaskCollab.dto.ActiveUserDTO;
import com.TaskCollab.dto.TaskDTO;
<<<<<<< Updated upstream

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

=======
import com.TaskCollab.dto.TaskStatisticsDTO;
>>>>>>> Stashed changes
import com.TaskCollab.Entity.TaskInterface;
import com.TaskCollab.Service.FetchDataService;
import com.TaskCollab.Service.SearchService;
import com.TaskCollab.Service.TaskService;
import com.TaskCollab.config.JwtProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
<<<<<<< Updated upstream
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap; // Import TypeMap
=======

>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
    @Autowired ModelMapper modelMapper;

    // Configure ModelMapper for TaskInterface to TaskDTO mapping
    // @Autowired
    // public TaskController(ModelMapper modelMapper) {
    //     this.modelMapper = modelMapper;
    //     TypeMap<TaskInterface, TaskDTO> typeMap = modelMapper.createTypeMap(TaskInterface.class, TaskDTO.class);
    // }

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
=======
    @Autowired
    private FetchDataService fetchDataService; // Injecting the service correctly   

    @PostMapping("/search") // Change to POST to accept JSON body
    public ResponseEntity<List<TaskDTO>> searchTasks(@RequestBody TaskDTO searchCriteria) { // Accept JSON body
        String taskTitle = searchCriteria.getTaskTitle();
        String description = searchCriteria.getDescription();
        String assignedTo = searchCriteria.getAssignedTo();
        String status = searchCriteria.getStatus();
        String deadline = null;
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
=======

    /*[DATA VISUALIZATION]: Return the statistics of Task.
       Suggestion:
        [] Create pie chart: Show visually visually the distribution of the Roles.
        [] Icons indicates the status: Showing the total of Completeted, Pending, In Progress task. 
    */
    @GetMapping("/statistics")
    public ResponseEntity<List<TaskStatisticsDTO>> getTaskStatistics() {
        List<TaskStatisticsDTO> taskStatistics = fetchDataService.getTaskStatistics();
        return ResponseEntity.ok(taskStatistics);
    }

    /*[DATA VISUALIZATION]: Return the top 5 most active users. 
       Suggestion:
        [] A billboard: Show visually the ranking of the users.
        [] Filters: Sort in descending order by
                    (1) The total completed task.
                    (2) The total task assigned to.
                    (3) The percentage of task completed. 
    */
    @GetMapping("/top5/{filterType}")
    public ResponseEntity<List<ActiveUserDTO>> getTop5UserWithMostTask(@PathVariable String filterType){
        List<ActiveUserDTO> activeUser = fetchDataService.getTop5UsersWithMostTasks(filterType); 
        return ResponseEntity.ok(activeUser);
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
>>>>>>> Stashed changes
}