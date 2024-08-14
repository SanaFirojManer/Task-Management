package com.task.manager.dev.sana.serviceImpl;


import com.task.manager.dev.sana.entity.User;
import com.task.manager.dev.sana.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setTimezone("UTC");
        user.setActive(true);

        // Simulate the repository save method
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        assertEquals(user, createdUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Doe");

        List<User> users = Arrays.asList(user1, user2);

        // Simulate the repository findAll method
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_UserExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        // Simulate the repository findById method returning the user
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertEquals(Optional.of(user), result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_UserDoesNotExist() {
        // Simulate the repository findById method returning empty
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<User> user = userService.getUserById(1L);

        // Assert
        assertTrue(user.isEmpty(), "Expected Optional to be empty when user does not exist");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUser_UserExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");

        User userDetails = new User();
        userDetails.setFirstName("Jane");
        userDetails.setLastName("Doe");
        userDetails.setTimezone("PST");
        userDetails.setActive(false);

        // Simulate the repository findById method returning the existing user
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Simulate the repository save method
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User updatedUser = userService.updateUser(1L, userDetails);

        // Assert
        assertEquals("Jane", updatedUser.getFirstName());
        assertEquals("Doe", updatedUser.getLastName());
        assertEquals("PST", updatedUser.getTimezone());
        assertEquals(false, updatedUser.isActive());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_UserDoesNotExist() {
        // Simulate the repository findById method returning empty
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User userDetails = new User();
        userDetails.setFirstName("Jane");
        userDetails.setLastName("Doe");
        userDetails.setTimezone("PST");
        userDetails.setActive(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, userDetails);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));  // Ensure that the save method was never called
    }

    @Test
    void testDeleteUser_UserExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        // Simulate the repository findById method returning the existing user
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).delete(user);  // Ensure that the delete method was called once
    }

    @Test
    void testDeleteUser_UserDoesNotExist() {
        // Simulate the repository findById method returning empty
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).delete(any(User.class));  // Ensure that the delete method was never called
    }
}
