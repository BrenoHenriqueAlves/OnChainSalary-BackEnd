package com.example.OnChainSalary.repository;

import com.example.OnChainSalary.model.EmployeeCompany;
import com.example.OnChainSalary.model.EmployeeCompanyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeCompanyRepository extends JpaRepository<EmployeeCompany, EmployeeCompanyId> {
    // Método que encontra todos os registros de EmployeeCompany para um determinado User (funcionário)
    List<EmployeeCompany> findByUserId(UUID userId);
    Optional<EmployeeCompany> findByUserIdAndCompanyId(UUID userId, UUID companyId);


}
