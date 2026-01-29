package com.example.demo.game.services;

import com.example.demo.game.Action;
import org.springframework.stereotype.Component;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class GameService {
    private final Map<Action, Service> actions;

    public GameService(List<Service> services) {
        this.actions = mapServices(services);
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

    public Service getServiceFor(Action action) {
        return actions.get(action);
    }

}
