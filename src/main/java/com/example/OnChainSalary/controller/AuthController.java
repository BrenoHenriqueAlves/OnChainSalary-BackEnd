package com.example.OnChainSalary.controller;

import com.example.OnChainSalary.dto.VerificationRequestDTO;
import com.example.OnChainSalary.model.User;
import com.example.OnChainSalary.service.EmailService;
import com.example.OnChainSalary.service.UserService;
import com.example.OnChainSalary.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService; // Serviço de envio de e-mails

    public AuthController(UserService userService, JwtUtil jwtUtil, EmailService emailService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Verifica se o e-mail já está registrado
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("E-mail já cadastrado.");
        }

        // Gera o código de verificação
        String verificationCode = userService.generateVerificationCode(user.getEmail());

        // Envia o e-mail com o código
        emailService.sendVerificationEmail(user.getEmail(), verificationCode, user.getName());

        return ResponseEntity.ok("Código de verificação enviado para o e-mail.");
    }

    // Alterando para receber o email e o código no corpo da requisição
    @PostMapping("/confirm-registration")
    public ResponseEntity<?> confirmRegistration(@RequestBody VerificationRequestDTO verificationRequestDTO) {
        String email = verificationRequestDTO.getEmail();
        String code = verificationRequestDTO.getCode();
        String name = verificationRequestDTO.getName(); // Certifique-se de passar o nome no DTO
        String password = verificationRequestDTO.getPassword(); // Certifique-se de passar a senha no DTO

        // Verifica se o código de verificação está correto
        boolean isVerified = userService.verifyCode(email, code);

        if (isVerified) {
            // Cria o usuário no banco de dados após a verificação do código
            User newUser = userService.finalizeRegistration(email, name, password);
            return ResponseEntity.ok(newUser);
        } else {
            return ResponseEntity.status(400).body("Código de verificação inválido ou expirado.");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> optionalUser = userService.findByEmail(loginUser.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (new BCryptPasswordEncoder().matches(loginUser.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getEmail());
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(401).body("Credenciais inválidas.");
    }
}
