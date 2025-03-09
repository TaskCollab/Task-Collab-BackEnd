package com.TaskCollab.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long task_Id;
    private String task_Title;
    private String description;
    private String assigned_To;
    private String status;
    private LocalDateTime deadline;
}
