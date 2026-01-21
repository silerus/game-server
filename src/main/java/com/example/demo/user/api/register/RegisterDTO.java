package com.example.demo.user.api.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "Password не может быть пустым") @Size(min = 6, message = "Password должен быть минимум 6 символов") String password,
        @NotBlank(message = "Email не может быть пустым") @Email(message = "Email должен быть валидным") String email) {

    public RegisterDTO(String password, String email) {
        this.password = password;
        this.email = email;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }
}
