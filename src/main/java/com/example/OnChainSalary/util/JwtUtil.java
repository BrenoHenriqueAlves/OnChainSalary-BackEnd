package com.example.OnChainSalary.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct; // Atualizado para Jakarta
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private Key secretKey;

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.expiration}")
    private long expirationTime; // Tempo de expiração em milissegundos

    @PostConstruct
    public void init() {
        // Decodifica a chave base64 e a converte para um formato que o JWT pode usar.
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.secretKey = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateToken(String email) {
        long now = System.currentTimeMillis();
        Date expirationDate = new Date(now + expirationTime); // Define a data de expiração

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(now)) // Data de emissão
                .setExpiration(expirationDate) // Data de expiração
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
