package com.example.demo.user.api.refresh;

import jakarta.validation.constraints.NotBlank;

public record RefreshDTO(@NotBlank(message = "RefreshToken не может быть пустым") String refreshToken) {

}
