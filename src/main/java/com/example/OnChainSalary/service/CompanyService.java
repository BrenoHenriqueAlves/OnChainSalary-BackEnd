package com.example.OnChainSalary.service;

import com.example.OnChainSalary.model.Company;
import com.example.OnChainSalary.model.User;
import com.example.OnChainSalary.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(UUID id) {
        return companyRepository.findById(id);
    }

    public void deleteCompany(UUID id) {
        companyRepository.deleteById(id);
    }

    public Company addEmployeeToCompany(Company company, int employeeCount) {
        company.setNumberOfEmployees(company.getNumberOfEmployees() + employeeCount);
        return companyRepository.save(company);
    }

    public List<Company> getCompaniesByRepresentative(UUID representativeId) {
        return companyRepository.findByRepresentativeId(representativeId);
    }
    public Optional<Company> findByCompanyRegistrationNumber(String companyRegistrationNumber) {
        return companyRepository.findByCompanyRegistrationNumber(companyRegistrationNumber);
    }


}
