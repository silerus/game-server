package com.example.demo.infrastructure.security;

import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

    private final RefreshTokenCrudRepository redisRepo;

    public RefreshTokenRepository(RefreshTokenCrudRepository redisRepo) {
        this.redisRepo = redisRepo;
    }

    public void save(RefreshToken refreshToken) {
        redisRepo.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return redisRepo.findByToken(token)
                .orElse(null);
    }

    public void deleteByToken(String token) {
        redisRepo.deleteByToken(token);
    }
}
