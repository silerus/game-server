package com.example.demo.user.infrastructure.persistence.user;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepositoryInterface extends JpaRepository<UserJpaEntity, UUID> {
    UserJpaEntity findByEmail(String email);
}
