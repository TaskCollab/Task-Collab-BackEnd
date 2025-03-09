package com.TaskCollab.Service;

import com.TaskCollab.dto.TaskDTO;
import com.TaskCollab.Entity.Task;
import com.TaskCollab.dao.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Retrieve a specific task by its ID
    public TaskDTO getTaskById(Long taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        return taskOpt.map(this::convertToDTO).orElse(null);
    }

    // Create a new task
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTask_Title(taskDTO.getTask_Title());
        task.setDescription(taskDTO.getDescription());
        task.setAssigned_To(taskDTO.getAssigned_To());
        task.setStatus(taskDTO.getStatus());
        task.setDeadline(taskDTO.getDeadline());

        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    // Update existing task by ID
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Optional<Task> existingTaskOpt = taskRepository.findById(taskId);
        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();
            existingTask.setTask_Title(taskDTO.getTask_Title());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setAssigned_To(taskDTO.getAssigned_To());
            existingTask.setStatus(taskDTO.getStatus());
            existingTask.setDeadline(taskDTO.getDeadline());

            Task updatedTask = taskRepository.save(existingTask);
            return convertToDTO(updatedTask);
        }
        return null;
    }

    // Delete task by ID
    public boolean deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }

    // Helper method to convert Entity -> DTO
    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setTask_Id(task.getTask_id());
        dto.setTask_Title(task.getTask_Title());
        dto.setDescription(task.getDescription());
        dto.setAssigned_To(task.getAssigned_To());
        dto.setStatus(task.getStatus());
        dto.setDeadline(task.getDeadline());
        return dto;
    }
}
