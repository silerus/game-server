package com.example.demo.user.infrastructure.persistence;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRepository;
import com.example.demo.user.domain.exception.UserAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepository implements UserRepository {

    private final JpaUserRepositoryInterface jpaRepo;
    private final UserMapper mapper;

    public JpaUserRepository(JpaUserRepositoryInterface jpaRepo, UserMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public @NotNull User createUser(User user) throws UserAlreadyExistsException {
        UserJpaEntity entity = mapper.toJpaEntity(user);
        UserJpaEntity savedEntity = jpaRepo.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
