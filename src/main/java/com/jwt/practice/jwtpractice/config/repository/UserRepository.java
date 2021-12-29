package com.jwt.practice.jwtpractice.config.repository;

import com.jwt.practice.jwtpractice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
