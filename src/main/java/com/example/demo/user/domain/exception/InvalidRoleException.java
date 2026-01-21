package com.example.demo.user.domain.exception;

import com.example.demo.infrastructure.exceptions.DomainInvariantViolation;

public class InvalidRoleException extends DomainInvariantViolation {
    public InvalidRoleException(String message) {
        super(message);
    }
}
