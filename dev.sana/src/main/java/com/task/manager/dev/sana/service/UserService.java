package com.task.manager.dev.sana.service;

import com.task.manager.dev.sana.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User createUser(User user);

    public List<User> getAllUsers();

    public Optional<User> getUserById(Long id);

    public User updateUser(Long id, User userDetails);

    public void deleteUser(Long id);
}
