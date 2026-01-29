package com.example.demo.character.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Character {
    private final UUID id;
    private final UUID user_id;
    private final String name;
    private final OffsetDateTime creationDate;
}
