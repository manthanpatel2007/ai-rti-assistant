package com.hacthon.ai_rti_assistant.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final String secret =
            "my-super-secret-key-for-ai-rti-assistant-2026";

    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    secret.getBytes(StandardCharsets.UTF_8)
            );


    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000L * 60 * 15
                        )
                )
                .signWith(key)
                .compact();
    }




    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }




    public boolean isTokenValid(
            String token,
            String email
    ) {

        try {

            String tokenEmail = extractEmail(token);

            return tokenEmail.equals(email);

        } catch (Exception e) {

            return false;
        }
    }
}