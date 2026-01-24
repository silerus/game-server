package com.example.demo.user.api.refresh;

import com.example.demo.infrastructure.security.TokenPair;
import com.example.demo.user.api.login.JwtAuthenticationResponse;
import com.example.demo.user.application.RefreshTokenUseCase;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshAction {
    private final RefreshTokenUseCase refreshTokenUseCase;

    public RefreshAction(RefreshTokenUseCase refreshTokenUseCase) {
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<@NotNull JwtAuthenticationResponse> refresh(@RequestBody @jakarta.validation.constraints.NotNull @Valid RefreshDTO dto) {
        TokenPair tokenPair = this.refreshTokenUseCase.refreshToken(dto.refreshToken());
        return ResponseEntity.ok(new JwtAuthenticationResponse(tokenPair.accessToken(),tokenPair.refreshToken()));
    }
}
