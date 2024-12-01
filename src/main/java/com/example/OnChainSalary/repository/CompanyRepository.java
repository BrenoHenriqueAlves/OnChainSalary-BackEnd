package com.example.OnChainSalary.repository;

import com.example.OnChainSalary.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    // Método que encontra todas as empresas onde o usuário é o representante
    List<Company> findByRepresentativeId(UUID representativeId);
    Optional<Company> findByCompanyRegistrationNumber(String companyRegistrationNumber);

}
