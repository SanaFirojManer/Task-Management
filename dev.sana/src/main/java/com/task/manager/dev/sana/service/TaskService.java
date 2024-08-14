package com.task.manager.dev.sana.service;

import com.task.manager.dev.sana.entity.Status;
import com.task.manager.dev.sana.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    public Task createTask(Task task) ;

    public List<Task> getAllTasks();

    public Optional<Task> getTaskById(Long id);

    public Task updateTask(Long id, Task taskDetails);

    public void deleteTask(Long id);
}
