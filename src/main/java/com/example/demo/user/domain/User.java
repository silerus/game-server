package com.example.demo.user.domain;

import com.example.demo.user.domain.exception.InvalidEmailException;
import com.example.demo.user.domain.exception.InvalidIdException;
import com.example.demo.user.domain.exception.InvalidPasswordException;

import java.util.Objects;
import java.util.UUID;

public final class User {

    private final UUID id;
    private final String email;
    private final transient String password;

    public User(UUID id, String email, String password) {
        if (id == null) {
            throw new InvalidIdException("ID не может быть пустым");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException("Email не может быть пустым");
        }
        if (password == null || password.isBlank()) {
            throw new InvalidPasswordException("Password hash не может быть пустым");
        }
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }
}
