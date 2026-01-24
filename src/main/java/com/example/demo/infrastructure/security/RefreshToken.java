package com.example.demo.infrastructure.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@Setter
@RedisHash(value = "refresh_tokens", timeToLive = 86400L)
public class RefreshToken {
    @Id private UUID id;
    @Indexed private UUID userId;
    @Indexed private String token;

    public RefreshToken(UUID userId, String token) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.token = token;
    }
}
