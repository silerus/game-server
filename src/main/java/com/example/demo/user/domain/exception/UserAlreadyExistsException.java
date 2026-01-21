package com.example.demo.user.domain.exception;

import com.example.demo.infrastructure.exceptions.DomainInvariantViolation;

public class UserAlreadyExistsException extends DomainInvariantViolation {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
