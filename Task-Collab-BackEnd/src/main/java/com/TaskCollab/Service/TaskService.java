package com.TaskCollab.Service;

import com.TaskCollab.Decorator.LoggingTaskDecorator;
import com.TaskCollab.Decorator.ValidationTaskDecorator;
import com.TaskCollab.dto.TaskDTO;

import jakarta.transaction.Transactional;

import com.TaskCollab.Entity.Task;
import com.TaskCollab.Entity.TaskInterface;
import com.TaskCollab.dao.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

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

        Task savedTask = taskRepository.save(task);

        TaskInterface decoratedTask = savedTask;
        decoratedTask = new LoggingTaskDecorator(decoratedTask);
        decoratedTask = new ValidationTaskDecorator(decoratedTask);
        return decoratedTask;
    }

    public TaskInterface updateTask(Long taskId, TaskDTO taskDTO) {
        Optional<Task> existingTaskOpt = taskRepository.findById(taskId);
        return existingTaskOpt.map(existingTask -> {
            existingTask.setTask_Title(taskDTO.getTaskTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setAssigned_To(taskDTO.getAssignedTo());
            existingTask.setStatus(taskDTO.getStatus());
            existingTask.setDeadline(taskDTO.getDeadline());

            Task updatedTask = taskRepository.save(existingTask);
            TaskInterface decoratedTask = updatedTask;
            decoratedTask = new LoggingTaskDecorator(decoratedTask);
            decoratedTask = new ValidationTaskDecorator(decoratedTask);
            return decoratedTask;
        }).orElse(null);
    }

    @Transactional
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

    private TaskDTO convertToDTO(TaskInterface task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getTask_Id()); // Corrected field name
        dto.setTaskTitle(task.getTask_Title()); // Corrected field name
        dto.setDescription(task.getDescription());
        dto.setAssignedTo(task.getAssigned_To()); // Corrected field name
        dto.setStatus(task.getStatus());
        dto.setDeadline(task.getDeadline());
        return dto;
    }
}