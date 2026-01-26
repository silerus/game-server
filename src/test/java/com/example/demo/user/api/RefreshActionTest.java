package com.example.demo.user.api;

import com.example.demo.infrastructure.security.TokenPair;
import com.example.demo.user.api.login.JwtAuthenticationResponse;
import com.example.demo.user.api.refresh.RefreshAction;
import com.example.demo.user.api.refresh.RefreshDTO;
import com.example.demo.user.application.RefreshTokenUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshActionTest {

    @Mock
    private RefreshTokenUseCase refreshTokenUseCase;

    @InjectMocks
    private RefreshAction refreshAction;

    private TokenPair tokenPair;

    @BeforeEach
    void setUp() {
        tokenPair = new TokenPair("new-access-token", "new-refresh-token");
    }

    @Test
    void shouldReturnNewJwtTokensOnRefresh() {
        String refreshToken = "existing-refresh-token";
        RefreshDTO dto = new RefreshDTO(refreshToken);
        when(refreshTokenUseCase.refreshToken(refreshToken)).thenReturn(tokenPair);
        ResponseEntity<JwtAuthenticationResponse> response = refreshAction.refresh(dto);
        assertThat(response.getStatusCode().is2xxSuccessful()).isEqualTo(true);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isEqualTo("new-access-token");
        assertThat(response.getBody().refreshToken()).isEqualTo("new-refresh-token");
        verify(refreshTokenUseCase).refreshToken(refreshToken);
        verifyNoMoreInteractions(refreshTokenUseCase);
    }
}
