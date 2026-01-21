package com.example.demo.user.application;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRepository;
import com.example.demo.user.domain.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String email, String password) throws UserAlreadyExistsException {
        User user = new User(UUID.randomUUID(), email, password);
        return Objects.requireNonNull(this.userRepository.register(user), "User не может быть null");
    }
}
