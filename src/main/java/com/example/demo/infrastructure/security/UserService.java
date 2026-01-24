package com.example.demo.infrastructure.security;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByEmail(username);
    }

    public User saveUser(User user) {
        return Objects.requireNonNull(this.userRepository.save(user), "User не может быть null");
    }

    public User findById(UUID id) {
        return this.userRepository.findById(id);
    }
}
