package com.example.demo.user.application;

import com.example.demo.infrastructure.security.JwtService;
import com.example.demo.infrastructure.security.TokenPair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RefreshTokenUseCase {
    private final JwtService jwtService;

    public RefreshTokenUseCase(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public TokenPair refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("RefreshToken must not be null or empty");
        }
        return jwtService.refreshToken(refreshToken);
    }
}
