package com.example.demo.user.domain;

import com.example.demo.user.domain.exception.UserAlreadyExistsException;
import org.jetbrains.annotations.NotNull;

public interface UserRepository {
    @NotNull User createUser(User user) throws UserAlreadyExistsException;
}
