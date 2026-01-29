package com.example.demo.game.commands;

import com.example.demo.game.Action;
import com.example.demo.game.PlayerEvent;

@org.springframework.stereotype.Service
public class MoveCommand implements Command {

    @Override
    public Action getProcessType() {
        return Action.MOVE;
    }

    @Override
    public void execute(PlayerEvent player) {
        System.out.println(player);
    }
}
