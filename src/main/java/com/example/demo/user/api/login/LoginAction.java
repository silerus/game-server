package com.example.demo.user.api.login;

import com.example.demo.user.application.LoginUserUseCase;
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
    public ResponseEntity<@org.jetbrains.annotations.NotNull String> login(@RequestBody @NotNull @Valid LoginDTO dto) {
        String token = this.loginUserUseCase.login(dto.email(), dto.password());
        return ResponseEntity.ok(token);
    }

}
