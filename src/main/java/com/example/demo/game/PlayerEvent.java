package com.example.demo.game;

import com.example.demo.actor.GameCommand;

public class PlayerEvent implements GameCommand {
    public final String playerId;
    public final String eventType;
    public final String payload;

    public PlayerEvent(String playerId, String eventType, String payload) {
        this.playerId = playerId;
        this.eventType = eventType;
        this.payload = payload;
    }
}
