package com.example.demo.infrastructure.security;

public record TokenPair(
        String accessToken,
        String refreshToken
) {}
