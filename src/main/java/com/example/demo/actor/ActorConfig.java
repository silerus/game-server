package com.example.demo.actor;

import com.example.demo.game.GameService;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.cluster.sharding.typed.javadsl.ClusterSharding;
import org.apache.pekko.cluster.sharding.typed.javadsl.Entity;
import org.apache.pekko.cluster.sharding.typed.javadsl.EntityTypeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActorConfig {

    private final GameService gameService;

    @Autowired
    public ActorConfig(GameService gameService) {
        this.gameService = gameService;
    }

    @Bean
    public ActorSystem<GameCommand> actorSystem() {
        Config config = ConfigFactory.load();
        return ActorSystem.create(
                GameSupervisor.create(gameService),
                "game-superviser",
                config
        );
    }

    @Bean
    public ClusterSharding clusterSharding(ActorSystem<GameCommand> system) {
        ClusterSharding sharding = ClusterSharding.get(system);
        EntityTypeKey<GameCommand> typeKey = EntityTypeKey.create(GameCommand.class, "Player");
        sharding.init(Entity.of(typeKey, entityContext -> PlayerActor.create(entityContext.getEntityId(), gameService)));
        return sharding;
    }
}
