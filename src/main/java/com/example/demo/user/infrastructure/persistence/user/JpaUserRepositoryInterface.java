package com.example.demo.user.infrastructure.persistence.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaUserRepositoryInterface extends JpaRepository<UserJpaEntity, UUID> {
    UserJpaEntity findByEmail(String email);
}
