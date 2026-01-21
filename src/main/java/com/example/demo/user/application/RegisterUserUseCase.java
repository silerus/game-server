package com.example.demo.user.application;

import com.example.demo.infrastructure.security.SecurityConfig;
import com.example.demo.user.domain.Role;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRepository;
import com.example.demo.user.domain.exception.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = securityConfig.getPasswordEncoder();
    }

    public User register(String email, String password) throws UserAlreadyExistsException {
        String encodedPassword = passwordEncoder.encode(password);
        Role role = Role.USER;
        User user = new User(UUID.randomUUID(), email, encodedPassword, role);
        return Objects.requireNonNull(this.userRepository.save(user), "User не может быть null");
    }
}
