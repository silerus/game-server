package com.example.demo.user.api.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserDTO {

    @NotBlank(message = "Password не может быть пустым")
    @Size(min = 6, message = "Password должен быть минимум 6 символов")
    private final String password;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть валидным")
    private final String email;

    CreateUserDTO(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
