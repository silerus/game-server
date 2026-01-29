package com.example.demo.game.commands;

import com.example.demo.game.Action;
import com.example.demo.game.PlayerEvent;

public class ConnectCommand implements Command {
    @Override
    public Action getProcessType() {
        return Action.CONNECT;
    }

    @Override
    public void execute(PlayerEvent player) {

    }
}
