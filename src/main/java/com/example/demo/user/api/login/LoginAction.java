package com.example.demo.user.api.login;

import com.example.demo.infrastructure.security.TokenPair;
import com.example.demo.user.application.LoginUserUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
public class LoginAction {

    private final LoginUserUseCase loginUserUseCase;

    public LoginAction(LoginUserUseCase loginUserUseCase) {
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<@org.jetbrains.annotations.NotNull JwtAuthenticationResponse> login(@RequestBody @NotNull @Valid LoginDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Jwt jwt = (Jwt) auth.getPrincipal();
            String email = jwt.getSubject();
            String id = jwt.getClaim("id");
            String role = jwt.getClaimAsString("role");
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            System.out.println("Роли: " + authorities);
        }

        //Todo сделать ограничение в несколько попыток, прикрутить хранилище с блек листом
        //Todo прикрутить права вычисляемые по битовой маске
        TokenPair token = this.loginUserUseCase.login(dto.email(), dto.password());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token.accessToken(), token.refreshToken()));
    }

}
