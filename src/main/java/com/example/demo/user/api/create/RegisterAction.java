package com.example.demo.user.api.create;

import com.example.demo.user.application.RegisterUserUseCase;
import com.example.demo.user.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegisterAction {

    private final RegisterUserUseCase registerUserUseCase;
    private final PasswordEncoder passwordEncoder;

    public RegisterAction(RegisterUserUseCase registerUserUseCase, PasswordEncoder passwordEncoder) {
        this.registerUserUseCase = registerUserUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<@org.jetbrains.annotations.NotNull User> register(@RequestBody @NotNull @Valid RegisterDTO dto)  {
        String encodedPassword = passwordEncoder.encode(dto.password());
        User user = registerUserUseCase.register(dto.email(), encodedPassword);
        return ResponseEntity.ok(user);
    }
}
