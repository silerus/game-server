package com.example.demo.infrastructure.security;

import com.example.demo.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public JwtService (RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    public TokenPair generateToken(User user) {
        byte[] keyBytes = jwtSigningKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        String accessToken = this.generateAccessToken(user, key);
        this.removeOldRefreshTokens(user.getId());
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

    private void removeOldRefreshTokens(UUID userId) {
        List<RefreshToken> refreshToken = this.refreshTokenRepository.findAllByUserId(userId);
        refreshToken.forEach(refreshToken1 -> {
            this.refreshTokenRepository.delete(refreshToken1);
        });
    }

    private String generateRefreshToken(User user) {
        String newRefresh = UUID.randomUUID().toString();
        RefreshToken newEntity = new RefreshToken(user.getId(), newRefresh);
        refreshTokenRepository.save(newEntity);
        //todo создать шедулер чтобы чистил старые индексы
        return newRefresh;
    }

    public TokenPair refreshToken(String refreshToken) {
        RefreshToken token = this.refreshTokenRepository.findByToken(refreshToken);
        UUID userId = token.getUserId();
        User user = this.userService.findById(userId);
        byte[] keyBytes = jwtSigningKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        String accessToken = this.generateAccessToken(user, key);
        return new TokenPair(accessToken,token.getToken());
    }
}
