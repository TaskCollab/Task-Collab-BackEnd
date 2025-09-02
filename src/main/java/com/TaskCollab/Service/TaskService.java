package com.TaskCollab.Service;

import com.TaskCollab.Decorator.LoggingTaskDecorator;
import com.TaskCollab.Decorator.ValidationTaskDecorator;
import com.TaskCollab.dto.TaskDTO;
import com.TaskCollab.Entity.Task;
import com.TaskCollab.Entity.TaskInterface;
import com.TaskCollab.dao.TaskRepository;
import com.TaskCollab.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public TaskInterface getTaskById(Long taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        return taskOpt.map(task -> {
            TaskInterface decoratedTask = task;
            decoratedTask = new LoggingTaskDecorator(decoratedTask);
            decoratedTask = new ValidationTaskDecorator(decoratedTask);
            return decoratedTask;
        }).orElse(null);
    }

    public TaskInterface createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTask_Title(taskDTO.getTaskTitle());
        task.setDescription(taskDTO.getDescription());
        task.setAssigned_To(taskDTO.getAssignedTo());
        task.setStatus(taskDTO.getStatus());
        task.setDeadline(taskDTO.getDeadline());
        task.setLocked(false);

        Task savedTask = taskRepository.save(task);

        notificationService.createNotification(
                taskDTO.getAssignedTo(),
                "A new task was created with title - " + taskDTO.getTaskTitle(),
                "Information",
                "New Ticket was created for you");

        TaskInterface decoratedTask = savedTask;
        decoratedTask = new LoggingTaskDecorator(decoratedTask);
        decoratedTask = new ValidationTaskDecorator(decoratedTask);

        return decoratedTask;
    }

    public TaskInterface updateTask(Long taskId, TaskDTO taskDTO) {
        Optional<Task> existingTaskOpt = taskRepository.findById(taskId);

        return existingTaskOpt.map(existingTask -> {
            if (existingTask.isLocked() && !isAdmin()) {
                throw new RuntimeException("Task is locked and cannot be updated.");
            }

            existingTask.setTask_Title(taskDTO.getTaskTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setAssigned_To(taskDTO.getAssignedTo());
            existingTask.setStatus(taskDTO.getStatus());
            existingTask.setDeadline(taskDTO.getDeadline());
            existingTask.setLocked(taskDTO.isLocked());

            Task updatedTask = taskRepository.save(existingTask);

            notificationService.createNotification(
                    updatedTask.getAssigned_To(),
                    "Task '" + updatedTask.getTask_Title() + "' has been updated.",
                    "Task Update",
                    "Task Updated");

            TaskInterface decoratedTask = updatedTask;
            decoratedTask = new LoggingTaskDecorator(decoratedTask);
            decoratedTask = new ValidationTaskDecorator(decoratedTask);

            return decoratedTask;
        }).orElse(null);
    }

    public boolean deleteTask(Long taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);

        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            taskRepository.deleteById(taskId);

            notificationService.createNotification(
                    task.getAssigned_To(),
                    "Task '" + task.getTask_Title() + "' has been deleted.",
                    "Task Deletion",
                    "Task Deleted");

            return true;
        }

        return false;
    }

    public List<TaskInterface> getTasksByUsername(String username) {
        List<Task> tasks = taskRepository.findByAssigned_To(username);
        return tasks.stream().map(task -> {
            TaskInterface decoratedTask = task;
            decoratedTask = new LoggingTaskDecorator(decoratedTask);
            decoratedTask = new ValidationTaskDecorator(decoratedTask);
            return decoratedTask;
        }).collect(Collectors.toList());
    }

    @Transactional
    public String toggleLockStatus(Long taskId, boolean lock) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            throw new RuntimeException("Task not found.");
        }

        Task task = taskOpt.get();
        if (!isAdmin()) {
            throw new SecurityException("Only admins can lock or unlock tasks.");
        }

        task.setLocked(lock);
        taskRepository.save(task);
        return lock ? "Task has been locked successfully." : "Task has been unlocked successfully.";
    }

    private boolean isAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }
}
