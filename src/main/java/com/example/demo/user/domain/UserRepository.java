package com.example.demo.user.domain;

import com.example.demo.user.domain.exception.UserAlreadyExistsException;
import com.example.demo.user.domain.exception.UserNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UserRepository {
    @NotNull User save(User user) throws UserAlreadyExistsException;
    @NotNull User findByEmail (String email) throws UserNotFoundException;
    @NotNull User findById (UUID id) throws UserNotFoundException;
}
