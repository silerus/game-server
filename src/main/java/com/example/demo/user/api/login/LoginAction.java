package com.example.demo.user.api.login;

import com.example.demo.infrastructure.security.TokenPair;
import com.example.demo.user.application.LoginUserUseCase;
import com.example.demo.user.domain.exception.UserNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginAction {

    private final LoginUserUseCase loginUserUseCase;

    public LoginAction(LoginUserUseCase loginUserUseCase) {
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<@org.jetbrains.annotations.NotNull JwtAuthenticationResponse> login(@RequestBody @NotNull @Valid LoginDTO dto) throws UserNotFoundException {
        //Todo сделать ограничение в несколько попыток, прикрутить хранилище с блек листом
        //Todo прикрутить права вычисляемые по битовой маске
        TokenPair token = this.loginUserUseCase.login(dto.email(), dto.password());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token.accessToken(), token.refreshToken()));
    }

}
