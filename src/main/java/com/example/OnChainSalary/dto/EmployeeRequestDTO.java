package com.example.OnChainSalary.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class EmployeeRequestDTO {

    private UUID userId;
    private String role;
    private BigDecimal salary;
    private UUID companyId;  // Adiciona o campo companyId

    // Getters e setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
}
