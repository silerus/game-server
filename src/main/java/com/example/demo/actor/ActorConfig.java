package com.example.demo.actor;

import com.example.demo.game.services.GameService;
import org.apache.pekko.actor.typed.ActorSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActorConfig {
    @Bean
    public ActorSystem<GameCommand> actorSystem(GameService gameService) {
        return ActorSystem.create(
                GameSupervisor.create(gameService),
                "game-superviser"
        );
    }
}
