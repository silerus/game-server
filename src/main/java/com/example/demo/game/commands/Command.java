package com.example.demo.game.commands;

import com.example.demo.game.Action;
import com.example.demo.game.PlayerEvent;

public interface Command {
    public Action getProcessType();
    public void execute(PlayerEvent player);
}
