package com.example.demo.user.application;

import com.example.demo.config.SecurityConfig;
import com.example.demo.infrastructure.security.UserService;
import com.example.demo.user.domain.Role;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseTest {
    @Mock
    UserService userService;

    @Mock
    SecurityConfig securityConfig;

    @Mock
    PasswordEncoder passwordEncoder;

    RegisterUserUseCase useCase;

    @BeforeEach
    void setup() {
        when(securityConfig.getPasswordEncoder()).thenReturn(passwordEncoder);
        useCase = new RegisterUserUseCase(userService, securityConfig);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        String email = "test@mail.com";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        doAnswer(invocation -> invocation.getArgument(0))
                .when(userService).saveUser(any(User.class));
        User result = useCase.register(email, rawPassword);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(encodedPassword);
        assertThat(result.getRole()).isEqualTo(Role.USER);
        verify(passwordEncoder).encode(rawPassword);
        verify(userService).saveUser(any(User.class));
    }

    @Test
    void shouldThrowWhenUserAlreadyExists() {
        String email = "test@mail.com";
        String rawPassword = "password";
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");
        when(userService.saveUser(any(User.class))).thenThrow(new UserAlreadyExistsException("User exists"));
        assertThatThrownBy(() -> useCase.register(email, rawPassword))
                .isInstanceOf(UserAlreadyExistsException.class);
        verify(userService).saveUser(any(User.class));
    }

    @Test
    void loginWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> useCase.register("abc", ""));
        assertThrows(IllegalArgumentException.class, () -> useCase.register("", "abc"));
        assertThrows(IllegalArgumentException.class, () -> useCase.register(null, "abc"));
        assertThrows(IllegalArgumentException.class, () -> useCase.register("abc", null));
    }
}
