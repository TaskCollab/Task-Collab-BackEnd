package com.TaskCollab.Service;

import com.TaskCollab.dto.TaskDTO;
import com.TaskCollab.Entity.Task;
import com.TaskCollab.dao.TaskRepository;
import com.TaskCollab.Util.TaskBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public TaskDTO getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(this::convertToDTO).orElse(null);
    }

    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setTask_Title(taskDTO.getTaskTitle());
            task.setDescription(taskDTO.getDescription());
            task.setAssigned_To(taskDTO.getAssignedTo());
            task.setStatus(taskDTO.getStatus());
            task.setDeadline(taskDTO.getDeadline());
            Task updatedTask = taskRepository.save(task);
            return convertToDTO(updatedTask);
        }
        return null;
    }
    
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new TaskBuilder()
                        .withTitle(taskDTO.getTaskTitle())
                        .withDescription(taskDTO.getDescription())
                        .withAssignedTo(taskDTO.getAssignedTo())
                        .withStatus(taskDTO.getStatus())
                        .withDeadline(taskDTO.getDeadline())
                        .build();
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }
    
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getTask_id());
        dto.setTaskTitle(task.getTask_Title());
        dto.setDescription(task.getDescription());
        dto.setAssignedTo(task.getAssigned_To());
        dto.setStatus(task.getStatus());
        dto.setDeadline(task.getDeadline());
        return dto;
    }
}
