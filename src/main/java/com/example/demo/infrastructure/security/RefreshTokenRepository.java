package com.example.demo.infrastructure.security;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class RefreshTokenRepository {

    private final RefreshTokenCrudRepository redisRepo;

    public RefreshTokenRepository(RefreshTokenCrudRepository redisRepo) {
        this.redisRepo = redisRepo;
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return redisRepo.save(refreshToken);
    }

    public List<RefreshToken> findAllByUserId(UUID userId) {
        return redisRepo.findAllByUserId(userId);
    }

    public RefreshToken findByToken(String token) {
        return redisRepo.findByToken(token)
                .orElse(null);
    }

    public void deleteAll(List<RefreshToken> tokens) {
        redisRepo.deleteAll(tokens);
    }
}
