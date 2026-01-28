package com.example.demo.game.services;

import com.example.demo.game.Action;
import com.example.demo.game.PlayerEvent;

import java.util.List;

public interface Service {
    public List<Action> getProcessTypes();
    public void run(PlayerEvent player);
}
