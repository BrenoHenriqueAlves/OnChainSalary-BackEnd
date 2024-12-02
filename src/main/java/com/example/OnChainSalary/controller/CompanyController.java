package com.example.OnChainSalary.controller;

import com.example.OnChainSalary.dto.EmployeeRequestDTO;
import com.example.OnChainSalary.model.Company;
import com.example.OnChainSalary.model.EmployeeCompany;
import com.example.OnChainSalary.model.User;
import com.example.OnChainSalary.service.CompanyService;
import com.example.OnChainSalary.service.EmployeeCompanyService;
import com.example.OnChainSalary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;
    private final EmployeeCompanyService employeeCompanyService;

    public CompanyController(CompanyService companyService, UserService userService, EmployeeCompanyService employeeCompanyService) {
        this.companyService = companyService;
        this.userService = userService;
        this.employeeCompanyService = employeeCompanyService;
    }

    // Rota para adicionar uma nova empresa
    @PostMapping("/add")
    public ResponseEntity<?> addCompany(@RequestBody Company company, Authentication authentication) {
        // Verifica se a empresa com o mesmo companyRegistrationNumber já existe
        Optional<Company> existingCompany = companyService.findByCompanyRegistrationNumber(company.getCompanyRegistrationNumber());

        if (existingCompany.isPresent()) {
            // Retorna um erro 400 (Bad Request) com a mensagem personalizada
            return ResponseEntity.status(400).body("Já existe uma empresa com o mesmo número de registro.");
        }

        // Se não existir, prossegue com a criação da empresa
        User representative = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        company.setRepresentative(representative);
        Company savedCompany = companyService.addCompany(company);

        // Retorna a empresa cadastrada com o status 201 (Created)
        return ResponseEntity.status(201).body(savedCompany);
    }

    // Rota para listar as empresas onde o usuário logado é representante
    @GetMapping("/list-representative")
    public ResponseEntity<List<Company>> getCompaniesByRepresentative(Authentication authentication) {
        User authenticatedUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        List<Company> companies = companyService.getCompaniesByRepresentative(authenticatedUser.getId());
        return ResponseEntity.ok(companies);
    }

    // Rota para listar as empresas onde o usuário logado é funcionário
    @GetMapping("/list-employee")
    public ResponseEntity<List<Company>> getCompaniesByEmployee(Authentication authentication) {
        User authenticatedUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        List<Company> companies = employeeCompanyService.getCompaniesByEmployee(authenticatedUser.getId());
        return ResponseEntity.ok(companies);
    }

    // Rota para deletar uma empresa
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok("Company deleted successfully");
    }

    // Rota para adicionar um funcionário à empresa
    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployeeToCompany(
            @RequestBody EmployeeRequestDTO employeeRequestDTO,
            Authentication authentication) {

        try {
            UUID companyId = employeeRequestDTO.getCompanyId();
            Optional<Company> companyOptional = companyService.getCompanyById(companyId);
            if (companyOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Company company = companyOptional.get();

            // Verifica se o usuário autenticado é o representante da empresa
            User authenticatedUser = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            if (!company.getRepresentative().getId().equals(authenticatedUser.getId())) {
                return ResponseEntity.status(403).body("Você não tem permissão para adicionar funcionários a esta empresa.");
            }

            // Adiciona o funcionário à empresa
            EmployeeCompany employeeCompany = employeeCompanyService.addEmployeeToCompany(
                    employeeRequestDTO.getUserId(), companyId, employeeRequestDTO.getRole(), employeeRequestDTO.getSalary());

            return ResponseEntity.ok(employeeCompany);

        } catch (RuntimeException ex) {
            // Se for uma exceção do tipo RuntimeException (como funcionário já cadastrado)
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }
}
