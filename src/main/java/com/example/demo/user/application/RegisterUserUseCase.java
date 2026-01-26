package com.example.demo.user.application;

import com.example.demo.infrastructure.security.SecurityConfig;
import com.example.demo.infrastructure.security.UserService;
import com.example.demo.user.domain.Role;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.exception.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional
public class RegisterUserUseCase {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserService userService, SecurityConfig securityConfig) {
        this.userService = userService;
        this.passwordEncoder = securityConfig.getPasswordEncoder();
    }

    public User register(String email, String password) throws UserAlreadyExistsException {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
        String encodedPassword = passwordEncoder.encode(password);
        Role role = Role.USER;
        User user = new User(UUID.randomUUID(), email, encodedPassword, role);
        return this.userService.saveUser(user);
    }
}
