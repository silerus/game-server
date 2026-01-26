package com.example.demo.user.api;

import com.example.demo.infrastructure.security.TokenPair;
import com.example.demo.user.api.login.JwtAuthenticationResponse;
import com.example.demo.user.api.login.LoginAction;
import com.example.demo.user.api.login.LoginDTO;
import com.example.demo.user.application.LoginUserUseCase;
import com.example.demo.user.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginActionTest {

    @Mock
    LoginUserUseCase loginUserUseCase;
    @InjectMocks
    LoginAction loginAction;

    @Test
    void shouldReturnJwtTokensOnSuccessfulLogin() throws UserNotFoundException {
        String email = "user@test.com";
        String password = "password";
        LoginDTO dto = new LoginDTO(password, email);
        TokenPair tokenPair = new TokenPair("access-token", "refresh-token");
        when(loginUserUseCase.login(email, password)).thenReturn(tokenPair);
        ResponseEntity<JwtAuthenticationResponse> response = loginAction.login(dto);
        assertThat(response.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isEqualTo("access-token");
        assertThat(response.getBody().refreshToken()).isEqualTo("refresh-token");
        verify(loginUserUseCase).login(email, password);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenLoginFails() throws UserNotFoundException {
        String email = "user@test.com";
        String password = "wrongpassword";
        LoginDTO dto = new LoginDTO(password, email);
        when(loginUserUseCase.login(email, password))
                .thenThrow(new UserNotFoundException("User not found"));
        assertThatThrownBy(() -> loginAction.login(dto))
                .isInstanceOf(UserNotFoundException.class);
        verify(loginUserUseCase).login(email, password);
    }
}
