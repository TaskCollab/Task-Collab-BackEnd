package com.TaskCollab.Service;

import com.TaskCollab.Decorator.LoggingTaskDecorator;
import com.TaskCollab.Decorator.ValidationTaskDecorator;
import com.TaskCollab.dto.TaskDTO;
import com.TaskCollab.Entity.Task;
import com.TaskCollab.Entity.TaskInterface;
import com.TaskCollab.Entity.Users;
import com.TaskCollab.dao.TaskRepository;
import com.TaskCollab.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Retrieve a specific task by its ID
    public TaskInterface getTaskById(Long taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        return taskOpt.map(task -> {
            TaskInterface decoratedTask = task;
            decoratedTask = new LoggingTaskDecorator(decoratedTask);
            decoratedTask = new ValidationTaskDecorator(decoratedTask);
            return decoratedTask;
        }).orElse(null);
    }

    // Create a new task
    public TaskInterface createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTask_Title(taskDTO.getTaskTitle());
        task.setDescription(taskDTO.getDescription());
        task.setAssigned_To(taskDTO.getAssignedTo());
        task.setStatus(taskDTO.getStatus());
        task.setDeadline(taskDTO.getDeadline());

        Task savedTask = taskRepository.save(task);

        TaskInterface decoratedTask = savedTask;
        decoratedTask = new LoggingTaskDecorator(decoratedTask);
        decoratedTask = new ValidationTaskDecorator(decoratedTask);
        return decoratedTask;
    }

    // Update existing task by ID
    public TaskInterface updateTask(Long taskId, TaskDTO taskDTO) {
        Optional<Task> existingTaskOpt = taskRepository.findById(taskId);

        return existingTaskOpt.map(existingTask -> {
            existingTask.setTask_Title(taskDTO.getTaskTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setAssigned_To(taskDTO.getAssignedTo());
            existingTask.setStatus(taskDTO.getStatus());
            existingTask.setDeadline(taskDTO.getDeadline());

            Task updatedTask = taskRepository.save(existingTask);

            // Lookup user by username to get userId for notification
            Users user = userRepository.findByUsername(updatedTask.getAssigned_To()).orElse(null);
            if (user != null) {
                notificationService.sendNotification(
                    user.getUserId(),
                    "Task '" + updatedTask.getTask_Title() + "' has been updated."
                );
            }

            // Decorate the task
            TaskInterface decoratedTask = updatedTask;
            decoratedTask = new LoggingTaskDecorator(decoratedTask);
            decoratedTask = new ValidationTaskDecorator(decoratedTask);

            return decoratedTask;
        }).orElse(null);
    }

    // Delete task by ID
    public boolean deleteTask(Long task_Id) {
        if (taskRepository.findById(task_Id).isPresent()) {
            taskRepository.deleteById(task_Id);
            System.out.println("Deleted Task with ID: " + task_Id);
            return true;
        }
        System.out.println("Task ID " + task_Id + " not found.");
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
}
