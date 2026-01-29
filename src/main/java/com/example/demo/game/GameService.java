package com.example.demo.game;

import com.example.demo.game.commands.Command;
import org.springframework.stereotype.Component;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class GameService {
    private final Map<Action, Command> commands;

    public GameService(List<Command> commands) {
        this.commands = mapCommands(commands);
    }

    private Map<Action, Command> mapCommands(List<Command> commands) {
        Map<Action, Command> map = new EnumMap<>(Action.class);
        for (Command command : commands) {
            Action action = command.getProcessType();
            map.put(action, command);
        }
        return map;
    }

    public Command getHandlerFor(Action action) {
        return commands.get(action);
    }

}
