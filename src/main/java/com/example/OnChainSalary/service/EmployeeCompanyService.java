package com.example.OnChainSalary.service;

import com.example.OnChainSalary.model.Company;
import com.example.OnChainSalary.model.EmployeeCompany;
import com.example.OnChainSalary.model.User;
import com.example.OnChainSalary.repository.CompanyRepository;
import com.example.OnChainSalary.repository.EmployeeCompanyRepository;
import com.example.OnChainSalary.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeCompanyService {

    private final EmployeeCompanyRepository employeeCompanyRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public EmployeeCompanyService(EmployeeCompanyRepository employeeCompanyRepository, UserRepository userRepository, CompanyRepository companyRepository) {
        this.employeeCompanyRepository = employeeCompanyRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public EmployeeCompany addEmployeeToCompany(UUID userId, UUID companyId, String role, BigDecimal salary) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Verificar se o funcionário já está associado à empresa
        Optional<EmployeeCompany> existingEmployeeCompany = employeeCompanyRepository.findByUserIdAndCompanyId(userId, companyId);
        if (existingEmployeeCompany.isPresent()) {
            throw new RuntimeException("Funcionário já cadastrado a essa empresa.");
        }

        EmployeeCompany employeeCompany = new EmployeeCompany();
        employeeCompany.setUser(user);
        employeeCompany.setCompany(company);
        employeeCompany.setRole(role);
        employeeCompany.setSalary(salary);

        return employeeCompanyRepository.save(employeeCompany);
    }

    public List<Company> getCompaniesByEmployee(UUID userId) {
        List<EmployeeCompany> employeeCompanies = employeeCompanyRepository.findByUserId(userId);
        return employeeCompanies.stream()
                .map(EmployeeCompany::getCompany)
                .collect(Collectors.toList());
    }


    // Método para listar as empresas nas quais o usuário é empregado


}
