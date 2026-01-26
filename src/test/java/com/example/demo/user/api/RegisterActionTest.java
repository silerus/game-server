package com.example.demo.user.api;

import com.example.demo.user.api.register.RegisterAction;
import com.example.demo.user.api.register.RegisterDTO;
import com.example.demo.user.application.RegisterUserUseCase;
import com.example.demo.user.domain.Role;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterActionTest {

    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @InjectMocks
    private RegisterAction registerAction;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(UUID.randomUUID(), "test@mail.com", "encodedPassword", Role.USER);
    }

    @Test
    void shouldReturnRegisteredUser() {
        RegisterDTO dto = new RegisterDTO("test@mail.com", "password");
        when(registerUserUseCase.register(dto.email(), dto.password())).thenReturn(user);
        ResponseEntity<User> response = registerAction.register(dto);
        assertThat(response.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(user);
        verify(registerUserUseCase).register(dto.email(), dto.password());
        verifyNoMoreInteractions(registerUserUseCase);
    }
}