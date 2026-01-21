package com.example.demo.user.domain.exception;

import com.example.demo.infrastructure.exceptions.DomainInvariantViolation;

public class UserNotFoundException extends DomainInvariantViolation {
    public UserNotFoundException(String message) {
        super(message);
    }
}
