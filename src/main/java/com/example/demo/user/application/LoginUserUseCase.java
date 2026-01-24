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
        userService.authenticate(email, password);
        User user = userService.getByEmail(email);
        TokenPair token = jwtService.generateToken(user);
        return token;
    }
}
