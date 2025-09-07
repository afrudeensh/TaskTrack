package com.sha.tasktrack.service.impl;

import com.sha.tasktrack.dto.TaskDto;
import com.sha.tasktrack.entity.Task;
import com.sha.tasktrack.entity.User;
import com.sha.tasktrack.enums.TaskStatus;
import com.sha.tasktrack.exception.BadRequestException;
import com.sha.tasktrack.exception.ResourceNotFoundException;
import com.sha.tasktrack.mapper.TaskMapper;
import com.sha.tasktrack.repository.TaskRepository;
import com.sha.tasktrack.repository.UserRepository;
import com.sha.tasktrack.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private void recalcActiveCount(User user) {
        long active = taskRepository.countByUserIdAndStatusNot(user.getId(), TaskStatus.COMPLETED);
        user.setTotalTasks((int) active);
        userRepository.save(user);
    }

    @Override
    public TaskDto update(Long id, TaskDto dto, Long userId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestException("Task not found"));

        if (!task.getUser().getId().equals(userId)) {
            throw new BadRequestException("You cannot update someone else's task");
        }

        if (dto.getTitle() != null) task.setTitle(dto.getTitle());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getStatus() != null) task.setStatus(dto.getStatus());
        if (dto.getPriority() != null) task.setPriority(dto.getPriority());
        if (dto.getDueDate() != null) task.setDueDate(dto.getDueDate());

        Task updatedTask = taskRepository.save(task);

        // ✅ Recalculate active task count whenever a task is updated
        recalcActiveCount(task.getUser());

        return TaskMapper.toDto(updatedTask);
    }


    @Override
    public void delete(Long id, Long userId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestException("Task not found"));

        if (!task.getUser().getId().equals(userId)) {
            throw new BadRequestException("You cannot delete someone else's task");
        }

        User user = task.getUser(); // get the owner
        taskRepository.delete(task);

        // 🔄 Recalculate active task count after deletion
        recalcActiveCount(user);
    }



    @Override
    @Transactional
    public TaskDto create(TaskDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        Task task = Task.builder().title(dto.getTitle()).description(dto.getDescription()).status(dto.getStatus()).priority(dto.getPriority()).dueDate(dto.getDueDate()).user(user).build();
        taskRepository.save(task);
        recalcActiveCount(user);
        return TaskMapper.toDto(task);
    }

    @Override
    public TaskDto getById(Long id, Long userId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));

        // Ensure the logged-in user owns this task
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to view this task");
        }

        return TaskMapper.toDto(task);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<TaskDto> list(Pageable pageable) {
        return taskRepository.findAll(pageable).map(TaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDto> searchByTitle(String title, Pageable pageable) {
        return taskRepository.findByTitleContainingIgnoreCase(title, pageable).map(TaskMapper::toDto);
    }
}
