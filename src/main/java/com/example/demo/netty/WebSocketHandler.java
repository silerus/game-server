package com.example.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@ChannelHandler.Sharable
@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final ConnectionGroup connectionGroup;
    private final RabbitPublisher rabbitPublisher;
    private final InstanceIdentity instanceIdentity;

    public WebSocketHandler(ConnectionGroup connectionGroup, RabbitPublisher rabbitPublisher, InstanceIdentity instanceIdentity) {
        this.connectionGroup = connectionGroup;
        this.rabbitPublisher = rabbitPublisher;
        this.instanceIdentity = instanceIdentity;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        if (webSocketFrame instanceof BinaryWebSocketFrame binaryFrame) {
            ByteBuf data = binaryFrame.content();
            byte[] bytes = new byte[data.readableBytes()];
            data.readBytes(bytes);
            handleBinary(channelHandlerContext, bytes);
        }  else {
            System.out.println("Unsupported frame type: " + webSocketFrame.getClass());
        }
    }

    protected void handleBinary(ChannelHandlerContext ctx, byte[] message) {
        System.out.println("Получено бинарное сообщение: " + message.length + " байт");
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(message);
            String routingKey = node.has("t") ? node.get("t").asText() : null;
            if (routingKey == null) {
                System.out.println("Поле t отсутствует, сообщение игнорируется");
                return;
            }
            JsonNode payloadNode = node.has("p") ? node.get("p") : mapper.createObjectNode();
            String instanceId = instanceIdentity.getId();
            AttributeKey<String> USER_ID_KEY = AttributeKey.valueOf("userId");
            String userId = ctx.channel().attr(USER_ID_KEY).get();
            ObjectNode finalNode = mapper.createObjectNode();
            finalNode.set("p", payloadNode);
            finalNode.put("instanceId", instanceId);
            finalNode.put("userId", userId);
            String finalMessage = mapper.writeValueAsString(finalNode);
            rabbitPublisher.sendIfQueueExists(routingKey, finalMessage.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void sendBinary(ChannelHandlerContext ctx, byte[] message) {
        ByteBuf buf = Unpooled.wrappedBuffer(message);
        ctx.writeAndFlush(new BinaryWebSocketFrame(buf));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился: " + ctx.channel().remoteAddress());
        connectionGroup.add(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент отключился: " + ctx.channel().remoteAddress());
        connectionGroup.remove(ctx.channel());
        super.channelInactive(ctx);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("Ошибка канала: " + cause.getMessage());
        ctx.close();
    }
}
