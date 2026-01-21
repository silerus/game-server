package com.example.demo.user.api.create;

import com.example.demo.user.application.CreateUserUseCase;
import com.example.demo.user.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CreateUserAction {

    private final CreateUserUseCase createUserUseCase;
    private final PasswordEncoder passwordEncoder;

    public CreateUserAction(CreateUserUseCase createUserUseCase, PasswordEncoder passwordEncoder) {
        this.createUserUseCase = createUserUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<@org.jetbrains.annotations.NotNull User> createUser(@RequestBody @NotNull @Valid CreateUserDTO dto)  {
        String encodedPassword = passwordEncoder.encode(dto.password());
        User user = createUserUseCase.createUser(dto.email(), encodedPassword);
        return ResponseEntity.ok(user);
    }
}
