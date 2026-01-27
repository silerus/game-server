package com.example.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final ConnectionGroup connectionGroup;

    public WebSocketHandler(ConnectionGroup connectionGroup) {
        this.connectionGroup = connectionGroup;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        if (webSocketFrame instanceof BinaryWebSocketFrame binaryFrame) {
            ByteBuf data = binaryFrame.content();
            byte[] bytes = new byte[data.readableBytes()];
            data.readBytes(bytes);
            handleBinary(channelHandlerContext, bytes);
        } else if (webSocketFrame instanceof TextWebSocketFrame textFrame) {
            String text = textFrame.text();
            handleText(channelHandlerContext, text);
        } else {
            System.out.println("Unsupported frame type: " + webSocketFrame.getClass());
        }
    }

    protected void handleBinary(ChannelHandlerContext ctx, byte[] message) {
        System.out.println("Получено бинарное сообщение: " + message.length + " байт");
        sendBinary(ctx, message);
    }

    protected void handleText(ChannelHandlerContext ctx, String message) {
        System.out.println("Получено текстовое сообщение: " + message);
        sendText(ctx, "Эхо: " + message);
    }

    protected void sendBinary(ChannelHandlerContext ctx, byte[] message) {
        ByteBuf buf = Unpooled.wrappedBuffer(message);
        ctx.writeAndFlush(new BinaryWebSocketFrame(buf));
    }

    protected void sendText(ChannelHandlerContext ctx, String message) {
        Attribute user = ctx.channel().attr(AttributeKey.valueOf("userId"));
        String messageStr = user.toString();
        ctx.writeAndFlush(new TextWebSocketFrame(messageStr));
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
