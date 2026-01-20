package com.example.demo.user.domain;

import java.util.UUID;

public final class User {

    private final UUID id;
    private final String email;
    private final transient String password;

    public User(UUID id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (password == null || !password.contains("@")) {
            throw new IllegalArgumentException("Password is invalid");
        }
    }

    public UUID id()       { return id; }
    public String email()  { return email; }
}
