package com.example.OnChainSalary.dto;

public class VerificationRequestDTO {
    private String email;
    private String code;
    private String name; // Nome do usuário
    private String password; // Senha do usuário
    // Construtores
    public VerificationRequestDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public VerificationRequestDTO(String email, String code) {
        this.email = email;
        this.code = code;
    }

    // Getters e Setters
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
}
