package com.example.application.data.repository;

import com.example.application.data.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc Repository für das Objekt User
 */
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}