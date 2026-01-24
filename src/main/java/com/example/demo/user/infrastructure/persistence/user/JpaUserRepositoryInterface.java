package com.example.demo.user.infrastructure.persistence.user;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaUserRepositoryInterface extends JpaRepository<UserJpaEntity, UUID> {
    UserJpaEntity findByEmail(String email);
}
