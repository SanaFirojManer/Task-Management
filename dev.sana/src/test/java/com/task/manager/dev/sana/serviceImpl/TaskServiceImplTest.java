package com.task.manager.dev.sana.serviceImpl;

import com.task.manager.dev.sana.entity.Status;
import com.task.manager.dev.sana.entity.Task;
import com.task.manager.dev.sana.entity.User;
import com.task.manager.dev.sana.repository.TaskRepository;
import com.task.manager.dev.sana.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        // Arrange
        Task task = new Task();
        task.setTitle("Complete Project Documentation");
        task.setDescription("Finish the documentation for the project.");

        // Simulate the repository save method
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setCreatedAt(Instant.now()); // Simulate @CreationTimestamp behavior
            savedTask.setUpdatedAt(Instant.now()); // Simulate @UpdateTimestamp behavior
            return savedTask;
        });

        // Act
        Task createdTask = taskService.createTask(task);

        // Assert
        assertEquals(Status.PENDING, createdTask.getStatus());
        assertEquals("Complete Project Documentation", createdTask.getTitle());
        assertEquals("Finish the documentation for the project.", createdTask.getDescription());
        assertNotNull(createdTask.getCreatedAt()); // Ensure createdAt is set
        assertNotNull(createdTask.getUpdatedAt()); // Ensure updatedAt is set
    }

    @Test
    void testGetAllTasks() {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description for Task 1");
        task1.setStatus(Status.PENDING);
        task1.setCreatedAt(Instant.now());
        task1.setUpdatedAt(Instant.now());

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description for Task 2");
        task2.setStatus(Status.COMPLETED);
        task2.setCreatedAt(Instant.now());
        task2.setUpdatedAt(Instant.now());

        List<Task> tasks = Arrays.asList(task1, task2);

        // Simulate the repository findAll method
        when(taskRepository.findAll()).thenReturn(tasks);

        // Act
        List<Task> retrievedTasks = taskService.getAllTasks();

        // Assert
        assertEquals(2, retrievedTasks.size());
        assertEquals("Task 1", retrievedTasks.get(0).getTitle());
        assertEquals("Task 2", retrievedTasks.get(1).getTitle());
    }

    @Test
    void testGetTaskById_TaskExists() {
        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setDescription("Description for Task 1");
        task.setStatus(Status.PENDING);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());

        // Simulate the repository findById method
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        Optional<Task> retrievedTask = taskService.getTaskById(1L);

        // Assert
        assertTrue(retrievedTask.isPresent());
        assertEquals("Task 1", retrievedTask.get().getTitle());
        assertEquals("Description for Task 1", retrievedTask.get().getDescription());
        assertEquals(Status.PENDING, retrievedTask.get().getStatus());
    }

    @Test
    void testGetTaskById_TaskDoesNotExist() {
        // Simulate the repository findById method returning empty
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Task> retrievedTask = taskService.getTaskById(1L);

        // Assert
        assertTrue(retrievedTask.isEmpty());
    }

    @Test
    void testUpdateTask_TaskExists() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("User");
        user1.setLastName("One");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("User");
        user2.setLastName("Two");

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus(Status.PENDING);
        existingTask.setAssignedTo(user1);
        existingTask.setCreatedAt(Instant.now());
        existingTask.setUpdatedAt(Instant.now());

        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("New Title");
        updatedTaskDetails.setDescription("New Description");
        updatedTaskDetails.setStatus(Status.IN_PROGRESS);
        updatedTaskDetails.setAssignedTo(user2);

        // Simulate the repository findById method returning the existing task
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // Simulate the repository save method
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        // Act
        Task updatedTask = taskService.updateTask(1L, updatedTaskDetails);

        // Assert
        assertEquals("New Title", updatedTask.getTitle());
        assertEquals("New Description", updatedTask.getDescription());
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus());
        assertEquals(user2, updatedTask.getAssignedTo());
        verify(taskRepository).save(existingTask);  // Ensure that the save method was called
    }

    @Test
    void testUpdateTask_TaskDoesNotExist() {
        // Simulate the repository findById method returning empty
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("New Title");
        updatedTaskDetails.setDescription("New Description");
        updatedTaskDetails.setStatus(Status.IN_PROGRESS);

        User user2 = new User();
        user2.setId(2L);
        updatedTaskDetails.setAssignedTo(user2);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(1L, updatedTaskDetails);
        });

        assertEquals("Task not found", exception.getMessage());
    }
    @Test
    void testDeleteTask_TaskExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setFirstName("User");
        user.setLastName("One");

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Sample Task");
        existingTask.setDescription("Sample Description");
        existingTask.setStatus(Status.PENDING);
        existingTask.setAssignedTo(user);
        existingTask.setCreatedAt(Instant.now());
        existingTask.setUpdatedAt(Instant.now());

        // Simulate the repository findById method returning the existing task
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).delete(existingTask);
    }

    @Test
    void testDeleteTask_TaskDoesNotExist() {
        // Simulate the repository findById method returning empty
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(1L);
        });

        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository, never()).delete(any(Task.class));
    }
}
