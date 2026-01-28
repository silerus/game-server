package com.example.demo.game.services;

import com.example.demo.game.Action;
import com.example.demo.game.PlayerEvent;

import java.util.List;

public class MovementService implements Service {
    @Override
    public List<Action> getProcessTypes() {
        return List.of(Action.MOVE, Action.IDLE);
    }

    @Override
    public void run(PlayerEvent player) {

    }
}
