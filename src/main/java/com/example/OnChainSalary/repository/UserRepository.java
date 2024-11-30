package com.example.OnChainSalary.repository;

import com.example.OnChainSalary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {  // Mude para UUID
    Optional<User> findByEmail(String email);
}
