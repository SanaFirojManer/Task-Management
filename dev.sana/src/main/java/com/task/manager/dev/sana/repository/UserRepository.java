package com.task.manager.dev.sana.repository;

import com.task.manager.dev.sana.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
