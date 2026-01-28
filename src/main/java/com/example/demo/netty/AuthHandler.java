package com.example.demo.netty;

import com.example.demo.infrastructure.security.JwtService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
public class AuthHandler extends ChannelInboundHandlerAdapter {

    private final JwtService jwtService;
    private final UserConnections userConnections;

    public AuthHandler(JwtService jwtService, UserConnections userConnections) {
        this.jwtService = jwtService;
        this.userConnections = userConnections;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            String authHeader = request.headers().get("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                sendUnauthorized(ctx);
                return;
            }
            String token = authHeader.substring(7);
            if (!jwtService.isValidToken(token)) {
                sendUnauthorized(ctx);
                return;
            }
            String userId = jwtService.getUserId(token);
            System.out.println("userID " + userId);
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
            userConnections.addUserConnection(userId, ctx);
        }
        super.channelRead(ctx, msg);
    }

    private void sendUnauthorized(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.UNAUTHORIZED
        );
        ctx.writeAndFlush(response).addListener(future -> ctx.close());
    }
}
