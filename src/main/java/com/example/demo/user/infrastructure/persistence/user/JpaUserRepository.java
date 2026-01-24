package com.example.demo.user.infrastructure.persistence.user;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRepository;
import com.example.demo.user.domain.exception.UserAlreadyExistsException;
import com.example.demo.user.domain.exception.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUserRepository implements UserRepository {

    private final JpaUserRepositoryInterface jpaRepo;
    private final UserMapper mapper;

    public JpaUserRepository(JpaUserRepositoryInterface jpaRepo, UserMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public @NotNull User save(User user) throws UserAlreadyExistsException {
        UserJpaEntity entity = mapper.toJpaEntity(user);
        try {
            UserJpaEntity saved = jpaRepo.saveAndFlush(entity);
            return mapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            // todo проверить что ошибка именно про поле email
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }
    }

    @Override
    public @NotNull User findByEmail(String email) throws UserNotFoundException {
        UserJpaEntity entity = jpaRepo.findByEmail(email);
        if (entity == null) throw new UserNotFoundException("Пользователь не найден");
        return mapper.toDomain(entity);
    }

    @Override
    public @NotNull User findById(UUID id) throws UserNotFoundException {
        Optional<UserJpaEntity> entity = jpaRepo.findById(id);
        if (!entity.isPresent()) throw new UserNotFoundException("Пользователь не найден");
        return mapper.toDomain(entity.get());
    }
}
