package com.sha.tasktrack.dto;

import com.sha.tasktrack.enums.TaskPriority;
import com.sha.tasktrack.enums.TaskStatus;
import lombok.*;

import java.time.LocalDate;

@Data               // generates getters + setters + toString + equals + hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder            // enables TaskDto.builder()
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private Long userId;
}
