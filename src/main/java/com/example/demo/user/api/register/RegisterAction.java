package com.example.demo.user.api.register;

import com.example.demo.user.application.RegisterUserUseCase;
import com.example.demo.user.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegisterAction {

    private final RegisterUserUseCase registerUserUseCase;

    public RegisterAction(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<@org.jetbrains.annotations.NotNull User> register(@RequestBody @NotNull @Valid RegisterDTO dto)  {
        User user = registerUserUseCase.register(dto.email(), dto.password());
        return ResponseEntity.ok(user);
    }
}
