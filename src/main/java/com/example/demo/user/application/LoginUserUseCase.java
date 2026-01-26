package com.example.demo.user.application;

import com.example.demo.infrastructure.security.JwtService;
import com.example.demo.infrastructure.security.TokenPair;
import com.example.demo.infrastructure.security.UserService;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginUserUseCase {

    private final JwtService jwtService;
    private final UserService userService;

    public LoginUserUseCase(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public TokenPair login(String email, String password) throws UserNotFoundException {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
        User user = userService.getByEmail(email);
        userService.authenticate(email, password);
        return jwtService.generateToken(user);
    }
}
