package com.example.demo.user.api.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotBlank(message = "Password не может быть пустым") @Size(min = 6, message = "Password должен быть минимум 6 символов") String password,
        @NotBlank(message = "Email не может быть пустым") @Email(message = "Email должен быть валидным") String email) {
}
