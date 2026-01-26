package com.example.demo.user.application;

import com.example.demo.infrastructure.security.JwtService;
import com.example.demo.infrastructure.security.TokenPair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenUseCaseTest {
    @Mock
    JwtService jwtService;
    @InjectMocks
    RefreshTokenUseCase refreshTokenUseCase;

    @Test
    void shouldRefreshSuccessfully() {
        String token = UUID.randomUUID().toString();
        TokenPair tokenPair = new TokenPair("access", "refresh");
        when(jwtService.refreshToken(token)).thenReturn(tokenPair);
        TokenPair result = refreshTokenUseCase.refreshToken(token);
        assertThat(result).isEqualTo(tokenPair);
    }

    @Test
    void shouldThrowExceptionWhenTokenIsEmpty() {
        String token = "";
        assertThatThrownBy(() -> refreshTokenUseCase.refreshToken(token))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoMoreInteractions(jwtService);
        assertThatThrownBy(() -> refreshTokenUseCase.refreshToken(null))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoMoreInteractions(jwtService);
    }
}
