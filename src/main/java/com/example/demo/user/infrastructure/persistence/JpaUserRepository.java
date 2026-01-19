package com.example.demo.user.infrastructure.persistence;


import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRepository;
import com.example.demo.user.domain.exception.UserAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JpaUserRepository implements UserRepository {
    @Override
    public @NotNull User createUser(User user) throws UserAlreadyExistsException {
        return new User(new UUID(123,321),"123","123");
    }
}
