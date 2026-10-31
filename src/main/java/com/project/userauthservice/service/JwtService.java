package com.project.userauthservice.service;

import com.project.userauthservice.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    
    private final SecretKey secretKey;
    private final long expirySeconds;


    public JwtService(@Value("${jwt.secret-key}") String secretKey,@Value("${jwt.expiration}") long expirySeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirySeconds = expirySeconds;
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .issuer("userAuthService")
                .expiration(new Date(System.currentTimeMillis() + expirySeconds*1000))
                .claim("role", user.getRole().name())
                .signWith(secretKey)
                .compact()
                ;
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                ;
    }
}
