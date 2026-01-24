package com.example.demo.infrastructure.security;

import com.example.demo.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public JwtService(RefreshTokenRepository refreshTokenRepository, UserService userService) {
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
        List<RefreshToken> oldTokens = refreshTokenRepository.findAllByUserId(userId);
        if (!oldTokens.isEmpty()) {
            refreshTokenRepository.deleteAll(oldTokens);
        }
    }

    private String generateRefreshToken(User user) {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        String refreshValue = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        RefreshToken entity = new RefreshToken(user.getId(), refreshValue);
        refreshTokenRepository.save(entity);
        return refreshValue;
    }

    public TokenPair refreshToken(String refreshToken) {
        RefreshToken token = this.refreshTokenRepository.findByToken(refreshToken);
        UUID userId = token.getUserId();
        User user = this.userService.findById(userId);
        return generateToken(user);
    }
}
