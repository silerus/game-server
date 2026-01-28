package com.example.demo.netty;

import com.example.demo.actor.GameCommand;
import com.example.demo.actor.GameSupervisor;
import com.example.demo.game.PlayerEvent;
import com.example.demo.game.services.GameService;
import com.example.demo.infrastructure.utils.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.Props;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@ChannelHandler.Sharable
@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final ObjectMapper mapper;
    private final UserConnections userConnections;
    private static final AttributeKey<String> USER_ID_KEY = AttributeKey.valueOf("userId");
    private final ActorRef<GameCommand> gameSupervisor;

    public WebSocketHandler(JsonUtil jsonUtil, UserConnections userConnections, ActorSystem<GameCommand> actorSystem, GameService gameService) {
        this.mapper = jsonUtil.mapper;
        this.userConnections = userConnections;
        this.gameSupervisor = actorSystem.systemActorOf(GameSupervisor.create(gameService), "game-supervisor", Props.empty());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        if (webSocketFrame instanceof BinaryWebSocketFrame binaryFrame) {
            ByteBuf data = binaryFrame.content();
            byte[] bytes = new byte[data.readableBytes()];
            data.readBytes(bytes);
            handleBinary(channelHandlerContext, bytes);
        } else {
            System.out.println("Unsupported frame type: " + webSocketFrame.getClass());
        }
    }

    protected void handleBinary(ChannelHandlerContext ctx, byte[] message) {
        try {
            JsonNode node = mapper.readTree(message);
            String eventType = node.has("t") ? node.get("t").asText() : "unknown";
            JsonNode payloadNode = node.has("p") ? node.get("p") : mapper.createObjectNode();
            String userId = ctx.channel().attr(USER_ID_KEY).get();
            gameSupervisor.tell(new PlayerEvent(userId, eventType, payloadNode.binaryValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void broadcastToClients(byte[] message) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(message);
//            JsonNode usersNode = root.has("u") ? root.get("u") : null;
//            if (usersNode == null || !usersNode.isArray()) return;
//            JsonNode payloadData = root.has("p") ? root.get("p") : mapper.createObjectNode();
//            byte[] payload = mapper.writeValueAsBytes(payloadData);
//            for (JsonNode userNode : usersNode) {
//                if (userNode.isNull()) continue;
//                String userId = userNode.asText();
//                ChannelHandlerContext ctx = userConnections.getUserConnection(userId);
//                if (ctx != null && ctx.channel().isActive()) {
//                    ctx.writeAndFlush(new BinaryWebSocketFrame(
//                            Unpooled.wrappedBuffer(payload)
//                    ));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    protected void sendBinary(ChannelHandlerContext ctx, byte[] message) {
//        ByteBuf buf = Unpooled.wrappedBuffer(message);
//        ctx.writeAndFlush(new BinaryWebSocketFrame(buf));
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился: " + ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент отключился: " + ctx.channel().remoteAddress());
        String userId = ctx.channel().attr(USER_ID_KEY).get();
        this.userConnections.removeUserConnection(userId);
        super.channelInactive(ctx);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("Ошибка канала: " + cause.getMessage());
        ctx.close();
    }
}
