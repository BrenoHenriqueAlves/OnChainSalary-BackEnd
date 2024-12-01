package com.example.OnChainSalary.controller;

import com.example.OnChainSalary.model.User;
import com.example.OnChainSalary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/get-user-details")
    public ResponseEntity<?> getUserDetails(@RequestBody Map<String, String> request, Authentication authentication) {
        String email = request.get("email");

        // Busca o usuário pelo email
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }

        // Retorna apenas o nome e ID do usuário
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());

        return ResponseEntity.ok(response);
    }
}
