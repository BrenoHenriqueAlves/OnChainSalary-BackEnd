package com.example.OnChainSalary.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String email;
    private String code;
    private LocalDateTime expiryDate;

    // Construtor padrão (obrigatório para JPA)
    public VerificationCode() {
    }

    // Construtor com parâmetros
    public VerificationCode(String email, String code, LocalDateTime expiryDate) {
        this.email = email;
        this.code = code;
        this.expiryDate = expiryDate;
    }

    // Getters e setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
