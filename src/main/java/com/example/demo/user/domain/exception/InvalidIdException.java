package com.example.demo.user.domain.exception;

import com.example.demo.infrastructure.exceptions.DomainInvariantViolation;

public class InvalidIdException extends DomainInvariantViolation {
    public InvalidIdException(String message) {
        super(message);
    }
}
