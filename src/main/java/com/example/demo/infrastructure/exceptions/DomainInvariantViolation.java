package com.example.demo.infrastructure.exceptions;

public class DomainInvariantViolation extends RuntimeException {
    public DomainInvariantViolation(String message) {
        super(message);
    }
}
