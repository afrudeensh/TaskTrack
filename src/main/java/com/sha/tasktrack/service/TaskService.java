package com.sha.tasktrack.service;

import com.sha.tasktrack.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskDto create(TaskDto dto, Long userId);
    TaskDto update(Long id, TaskDto dto, Long userId);
    void delete(Long id, Long userId);
    TaskDto getById(Long id,Long userId);
    Page<TaskDto> list(Pageable pageable);
    Page<TaskDto> searchByTitle(String title, Pageable pageable);
}
