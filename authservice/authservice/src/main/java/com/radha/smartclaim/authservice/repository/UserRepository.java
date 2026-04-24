package com.radha.smartclaim.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radha.smartclaim.authservice.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
