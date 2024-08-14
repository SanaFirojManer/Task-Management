package com.task.manager.dev.sana.repository;

import com.task.manager.dev.sana.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
