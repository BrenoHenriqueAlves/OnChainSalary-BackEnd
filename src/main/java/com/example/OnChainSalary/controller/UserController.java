package com.example.OnChainSalary.controller;

import com.example.OnChainSalary.model.User;
import com.example.OnChainSalary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/get-user-details")
    public ResponseEntity<?> getUserDetails(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("name", user.getName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }
    }

    @PostMapping("/update-ethereum-address")
    public ResponseEntity<?> updateEthereumAddress(@RequestBody Map<String, String> request, Authentication authentication) {
        String email = authentication.getName();  // Obtém o e-mail do usuário autenticado
        String ethereumAddress = request.get("ethereumAddress");

        // Verifica se o endereço Ethereum é válido
        if (!userService.isValidEthereumAddress(ethereumAddress)) {
            return ResponseEntity.badRequest().body("Endereço Ethereum inválido.");
        }

        boolean updated = userService.updateEthereumAddress(email, ethereumAddress);

        if (updated) {
            return ResponseEntity.ok("Endereço Ethereum atualizado com sucesso.");
        } else {
            return ResponseEntity.status(404).body("Usuário não encontrado ou falha ao atualizar o endereço.");
        }
    }

    @PostMapping("/get-ethereum-address")
    public ResponseEntity<?> getEthereumAddress(@RequestBody Map<String, String> request, Authentication authentication) {
        // O email é enviado no corpo da requisição
        String email = request.get("email");

        // Verifica se o usuário está autenticado
        String authenticatedEmail = authentication.getName();

        if (authenticatedEmail == null || authenticatedEmail.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Usuário não autenticado."));
        }

        // Verifica se o usuário autenticado é o mesmo que está sendo buscado
//        if (!authenticatedEmail.equals(email)) {
//            return ResponseEntity.status(403).body(Map.of("message", "Você não tem permissão para acessar este endereço Ethereum."));
//        }

        // Busca o usuário pelo email
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Verifica se o usuário tem um endereço Ethereum
            if (user.getEthereumAddress() != null && !user.getEthereumAddress().isEmpty()) {
                return ResponseEntity.ok(Map.of("ethereumAddress", user.getEthereumAddress()));
            } else {
                return ResponseEntity.status(404).body(Map.of("message", "Endereço Ethereum não encontrado."));
            }
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Usuário não encontrado."));
        }
    }



    public class ResponseError {
        private String message;
        private int code;

        public ResponseError(String message, int code) {
            this.message = message;
            this.code = code;
        }

        // Getters e setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

}
