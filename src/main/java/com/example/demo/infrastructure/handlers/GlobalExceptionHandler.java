package com.example.demo.infrastructure.handlers;

import com.example.demo.infrastructure.AppEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final AppEnvironment appEnvironment;

    public  GlobalExceptionHandler(AppEnvironment environment) {
        this.appEnvironment = environment;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleInvariantViolation(RuntimeException e) {
        if (this.appEnvironment.isProd()) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.toString());
    }
}
