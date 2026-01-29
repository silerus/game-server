package com.example.demo.actor;

import com.example.demo.game.Action;
import com.example.demo.game.PlayerEvent;
import com.example.demo.game.services.GameService;
import com.example.demo.game.services.Service;
import io.netty.channel.Channel;
import org.apache.pekko.actor.Cancellable;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;
import org.apache.pekko.cluster.sharding.typed.javadsl.EntityTypeKey;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PlayerActor extends AbstractBehavior<GameCommand> {

    private final String userId;
    private final GameService gameService;
    private Channel playerChannel;
    private final Deque<PlayerEvent> eventQueue = new ArrayDeque<>();
    private final Cancellable tickTask;

    public static EntityTypeKey<GameCommand> TYPE_KEY = EntityTypeKey.create(GameCommand.class, "Player");

    public static Behavior<GameCommand> create(String userId, GameService gameService) {
        return Behaviors.setup(ctx -> new PlayerActor(ctx, userId, gameService));
    }

    private PlayerActor(ActorContext<GameCommand> context, String userId, GameService gameService) {
        super(context);
        this.userId = userId;
        this.gameService = gameService;
        this.tickTask = context.getSystem().scheduler().scheduleAtFixedRate(
                Duration.ofMillis(60), Duration.ofMillis(60),
                () -> context.getSelf().tell(Tick.INSTANCE),
                context.getSystem().executionContext()
        );
    }

    @Override
    public Receive<GameCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(PlayerEvent.class, this::onPlayerEvent)
                .onMessage(Connect.class, this::onConnect)
                .onMessage(Disconnect.class, this::onDisconnect)
                .onMessage(Tick.class, this::onTick)
                .build();
    }

    private Behavior<GameCommand> onConnect(Connect cmd) {
        this.playerChannel = cmd.channel();
        // Загрузить состояние игрока из Redis/БД
        getContext().getLog().info("Player {} connected", userId);
        return this;
    }

    private Behavior<GameCommand> onPlayerEvent(PlayerEvent event) {
        eventQueue.add(event); // накапливаем
        return this;
    }

    private Behavior<GameCommand> onTick(Tick tick) {
        List<PlayerEvent> batch = new ArrayList<>();
        while (!eventQueue.isEmpty()) {
            batch.add(eventQueue.poll());
        }
        for (PlayerEvent event : batch) {
            try {
                Action action = Action.valueOf(event.eventType);
                Service service = gameService.getServiceFor(action);
                service.run(event);
            } catch (Exception e) {
                getContext().getLog().error("Error processing event", e);
            }
        }
        return this;
    }

    private Behavior<GameCommand> onDisconnect(Disconnect cmd) {
        // Сохранить состояние
        if (playerChannel != null) playerChannel = null;
        tickTask.cancel();
        return Behaviors.stopped();
    }
}
