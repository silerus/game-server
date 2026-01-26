package com.example.demo.user.application;

import com.example.demo.infrastructure.security.*;
import com.example.demo.user.domain.Role;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class LoginUserUseCaseTest {

    @Mock
    JwtService jwtService;

    @Mock
    UserService userService;

    @InjectMocks
    LoginUserUseCase useCase;

    @Test
    void shouldLoginUserSuccessfully() {
        UUID uuid = UUID.randomUUID();
        String email = "test@mail.com";
        String password = "password";
        Role role = Role.USER;
        User user = new User(uuid, email, password, role);
        TokenPair tokenPair = new TokenPair("access", "refresh");
        doNothing().when(userService).authenticate(email, password);
        when(userService.getByEmail(email)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(tokenPair);
        TokenPair result = useCase.login(email, password);
        assertThat(result).isEqualTo(tokenPair);
        verify(userService).authenticate(email, password);
        verify(userService).getByEmail(email);
        verify(jwtService).generateToken(user);
    }

    @Test
    void loginWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> useCase.login("abc", ""));
        assertThrows(IllegalArgumentException.class, () -> useCase.login("", "abc"));
        assertThrows(IllegalArgumentException.class, () -> useCase.login(null, "abc"));
        assertThrows(IllegalArgumentException.class, () -> useCase.login("abc", null));
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        String email = "missing@mail.com";
        String password = "password";
        doThrow(new UserNotFoundException(""))
                .when(userService)
                .getByEmail(email);
        assertThatThrownBy(() -> useCase.login(email, password))
                .isInstanceOf(UserNotFoundException.class);
        verify(userService).getByEmail(email);
        verifyNoMoreInteractions(userService, jwtService);
    }
}
