package com.example.demo.game;

import com.example.demo.actor.GameCommand;

public class PlayerEvent implements GameCommand {
    public final String playerId;
    public final String eventType;
    public final byte[] payload;

    public PlayerEvent(String playerId, String eventType, byte[] payload) {
        this.playerId = playerId;
        this.eventType = eventType;
        this.payload = payload;
    }
}
