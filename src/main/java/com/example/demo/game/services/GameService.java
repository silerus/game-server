package com.example.demo.game.services;

import com.example.demo.game.Action;
import com.example.demo.game.PlayerEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class GameService {
    private final Map<Action, Service> actions;
    private final List<PlayerEvent> playerEvents;

    public GameService(List<Service> services) {
        this.actions = mapServices(services);
        this.playerEvents = new ArrayList<>();
    }

    private Map<Action, Service> mapServices(List<Service> services) {
        Map<Action, Service> map = new EnumMap<>(Action.class);
        for (Service service : services) {
            service.getProcessTypes().forEach(processType -> {
                map.put(processType, service);
            });
        }
        return map;
    }

    public void processTick() {
        for (PlayerEvent event : playerEvents) {
            try {
                Action action = Action.valueOf(event.eventType);
                Service service = actions.get(action);
                service.run(event);
            } catch (IllegalArgumentException e) {
            }
        }
        playerEvents.clear();
    }


    public void enqueueEvent(PlayerEvent playerEvent) {
        this.playerEvents.add(playerEvent);
    }

}
