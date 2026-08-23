package com.hacthon.ai_rti_assistant.service;

import com.hacthon.ai_rti_assistant.entity.RefreshToken;
import com.hacthon.ai_rti_assistant.entity.User;
import com.hacthon.ai_rti_assistant.exception.BadRequestException;
import com.hacthon.ai_rti_assistant.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


    // =========================================================
    // CREATE REFRESH TOKEN
    // =========================================================

    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(
                UUID.randomUUID().toString()
        );

        refreshToken.setUser(user);

        refreshToken.setExpiresAt(
                LocalDateTime.now().plusDays(7)
        );

        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }


    // =========================================================
    // VERIFY REFRESH TOKEN
    // =========================================================

    public RefreshToken verifyRefreshToken(
            String token
    ) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "Invalid refresh token"
                                )
                        );

        if (refreshToken.isRevoked()) {

            throw new BadRequestException(
                    "Refresh token has been revoked"
            );
        }

        if (
                refreshToken
                        .getExpiresAt()
                        .isBefore(LocalDateTime.now())
        ) {

            throw new BadRequestException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }


    // =========================================================
    // REVOKE REFRESH TOKEN
    // =========================================================

    public void revokeToken(String token) {

        refreshTokenRepository
                .findByToken(token)
                .ifPresent(refreshToken -> {

                    refreshToken.setRevoked(true);

                    refreshTokenRepository.save(
                            refreshToken
                    );
                });
    }
}