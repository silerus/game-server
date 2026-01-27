package com.example.demo.netty;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class InstanceIdentity {
    private final String instanceId;

    public InstanceIdentity() {
        this.instanceId = Optional.ofNullable(System.getenv("POD_NAME")).orElse(UUID.randomUUID().toString());
    }

    public String getId() {
        return instanceId;
    }
}
