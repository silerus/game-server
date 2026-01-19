package com.example.demo.user.api.create;

import com.example.demo.user.application.CreateUserUseCase;
import com.example.demo.user.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CreateUserAction {

    private final CreateUserUseCase createUserUseCase;

    public CreateUserAction(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping("/users")
    public ResponseEntity<@org.jetbrains.annotations.NotNull User> createUser(@RequestBody @NotNull @Valid CreateUserDTO dto)  {
        User user = createUserUseCase.createUser(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(user);
    }
}
