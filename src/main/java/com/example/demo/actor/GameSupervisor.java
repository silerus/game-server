package com.example.demo.actor;

import com.example.demo.game.services.GameService;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

public class GameSupervisor extends AbstractBehavior<GameCommand> {
    private final GameService gameService;

    public static Behavior<GameCommand> create(GameService gameService) {
        return Behaviors.setup(ctx -> new GameSupervisor(ctx, gameService));
    }

    public GameSupervisor(ActorContext<GameCommand> context, GameService gameService) {
        super(context);
        this.gameService = gameService;
        context.getLog().info("GameSupervisor Application started");
    }

    @Override
    public Receive<GameCommand> createReceive() {
        return newReceiveBuilder().build();
    }
}