package com.example.demo.infrastructure.security;

import com.example.demo.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtService (RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public TokenPair generateToken(User user) {
        byte[] keyBytes = jwtSigningKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        String accessToken = this.generateAccessToken(user, key);
        String refreshToken = this.generateRefreshToken(user);
        return new TokenPair(accessToken, refreshToken);
    }

    private String generateAccessToken(User user, SecretKey key) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("id", user.getId())
                .claim("role", user.getRole())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private String generateRefreshToken(User user) {
        String newRefresh = UUID.randomUUID().toString();
        RefreshToken newEntity = new RefreshToken(user.getId(), newRefresh, Duration.ofDays(1));
        refreshTokenRepository.save(newEntity);
        return newRefresh;
    }
}
