package com.example.demo.user.domain.exception;

import com.example.demo.infrastructure.exceptions.DomainInvariantViolation;

public class InvalidPasswordException extends DomainInvariantViolation {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
