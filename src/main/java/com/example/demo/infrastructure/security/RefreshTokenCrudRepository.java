package com.example.demo.infrastructure.security;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenCrudRepository extends CrudRepository<RefreshToken, UUID>
{
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findAllByUserId(UUID userId);
}
