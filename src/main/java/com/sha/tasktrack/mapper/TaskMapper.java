package com.sha.tasktrack.mapper;

import com.sha.tasktrack.dto.TaskDto;
import com.sha.tasktrack.entity.Task;

public class TaskMapper {

    public static TaskDto toDto(Task task) {
        if (task == null) return null;
        return TaskDto.builder().id(task.getId()).title(task.getTitle()).description(task.getDescription()).status(task.getStatus()).priority(task.getPriority()).dueDate(task.getDueDate()).userId(task.getUser() != null ? task.getUser().getId() : null).build();
    }
}
