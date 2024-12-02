package com.example.OnChainSalary.repository;

import com.example.OnChainSalary.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {
    Optional<VerificationCode> findByEmailAndCode(String email, String code);
}
