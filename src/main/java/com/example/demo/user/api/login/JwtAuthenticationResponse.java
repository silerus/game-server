package com.example.demo.user.api.login;

import io.swagger.v3.oas.annotations.media.Schema;

public record JwtAuthenticationResponse(
        @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...") String accessToken,
        @Schema(description = "Токен для обновления токена доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...") String refreshToken) {
    public JwtAuthenticationResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public String accessToken() {
        return accessToken;
    }

    @Override
    public String refreshToken() {
        return refreshToken;
    }
}
