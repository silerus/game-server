package com.example.demo.user.domain.exception;

import com.example.demo.infrastructure.exceptions.DomainInvariantViolation;

public class InvalidEmailException extends DomainInvariantViolation {
    public InvalidEmailException(String message) {
        super(message);
    }
}
