package com.example.demo.infrastructure.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.Duration;
import java.util.UUID;

@Getter
@Setter
@RedisHash("refresh_tokens")  // ключи будут вида: refresh_tokens:<id>
public class RefreshToken {

    @Id
    private UUID id;
    private UUID userId;
    @Indexed
    private String token;
    private Instant expiresAt;
    @TimeToLive
    private Long ttl;

    public RefreshToken(UUID userId, String token, Duration ttl) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.token = token;
        this.expiresAt = Instant.now().plus(ttl);
        this.ttl = ttl.getSeconds();
    }
}
