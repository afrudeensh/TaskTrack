package com.sha.tasktrack.repository;

import com.sha.tasktrack.entity.Task;
import com.sha.tasktrack.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    long countByUserIdAndStatusNot(Long userId, TaskStatus status);
}
