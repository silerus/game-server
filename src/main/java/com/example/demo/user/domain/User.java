package com.example.demo.user.domain;

import java.util.UUID;

public record User(UUID id, String email, String password) {

    public User {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (password == null || !password.contains("@")) {
            throw new IllegalArgumentException("Password is invalid");
        }
    }
}
