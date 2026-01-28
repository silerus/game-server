package com.example.demo.actor;

import com.example.demo.game.PlayerEvent;
import com.example.demo.game.services.GameService;
import org.apache.pekko.actor.Cancellable;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.PostStop;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

public class GameSupervisor extends AbstractBehavior<GameCommand> {

    private final GameService gameService;
    private final Cancellable tickTask;

    public static Behavior<GameCommand> create(GameService gameService) {
        return Behaviors.setup(ctx -> new GameSupervisor(ctx, gameService));
    }

    public GameSupervisor(ActorContext<GameCommand> context, GameService gameService) {
        super(context);
        this.gameService = gameService;
        context.getLog().info("GameSupervisor Application started");
        this.tickTask = context.getSystem().scheduler().scheduleAtFixedRate(
                java.time.Duration.ofMillis(60),
                java.time.Duration.ofMillis(60),
                () -> context.getSelf().tell(Tick.INSTANCE),
                context.getSystem().executionContext()
        );
    }

    @Override
    public Receive<GameCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Tick.class, this::onTick)
                .onMessage(PlayerEvent.class, this::onPlayerEvent)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<GameCommand> onTick(Tick tick) {
        gameService.processTick();
        return this;
    }

    private Behavior<GameCommand> onPlayerEvent(PlayerEvent event) {
        gameService.enqueueEvent(event);
        return this;
    }

    private GameSupervisor onPostStop() {
        getContext().getLog().info("GameSupervisor Application stopped");
        tickTask.cancel();
        return this;
    }
}