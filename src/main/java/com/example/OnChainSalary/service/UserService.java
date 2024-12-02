package com.example.OnChainSalary.service;

import com.example.OnChainSalary.model.User;
import com.example.OnChainSalary.model.VerificationCode;
import com.example.OnChainSalary.repository.UserRepository;
import com.example.OnChainSalary.repository.VerificationCodeRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder; // Alterado para injeção do Spring

    // Construtor com injeção das dependências
    public UserService(UserRepository userRepository, VerificationCodeRepository verificationCodeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.passwordEncoder = passwordEncoder; // Usando o PasswordEncoder injetado
    }

    // Gera o código de verificação e salva
    public String generateVerificationCode(String email) {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        VerificationCode verificationCode = new VerificationCode(email, code, LocalDateTime.now().plusMinutes(15));
        verificationCodeRepository.save(verificationCode);
        return code;
    }

    // Verifica o código e remove se for válido
    public boolean verifyCode(String email, String code) {
        Optional<VerificationCode> verificationCodeOpt = verificationCodeRepository.findByEmailAndCode(email, code);

        if (verificationCodeOpt.isPresent()) {
            VerificationCode verificationCode = verificationCodeOpt.get();
            if (verificationCode.getExpiryDate().isAfter(LocalDateTime.now())) {
                verificationCodeRepository.delete(verificationCode); // Código válido, removendo
                return true;
            }
        }
        return false;
    }

    // Finaliza o registro do usuário
    public User finalizeRegistration(String email, String name, String password) {
        // Criptografa a senha antes de salvar
        String encryptedPassword = passwordEncoder.encode(password);  // Criptografando a senha

        // Cria um novo usuário com os dados fornecidos
        User user = new User(email, encryptedPassword, name);  // Usando o construtor com parâmetros

        // Salva o usuário no banco de dados
        return userRepository.save(user); // Salva o usuário e retorna ele
    }


    // Busca usuário pelo e-mail
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }




    public boolean updateEthereumAddress(String email, String ethereumAddress) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEthereumAddress(ethereumAddress);
            userRepository.save(user); // Atualiza no banco de dados
            return true;
        }
        return false; // Se o usuário não for encontrado
    }

    public boolean isValidEthereumAddress(String address) {
        // Verificação do formato: começa com "0x" e tem exatamente 40 caracteres hexadecimais
        String regex = "^0x[a-fA-F0-9]{40}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(address).matches();
    }

    // Atualiza o endereço Ethereum do usuário

}
