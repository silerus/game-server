package com.example.demo.netty;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserConnections {
    private final Map<String, ChannelHandlerContext> userConnections = new ConcurrentHashMap<>();

    public void addUserConnection(String userId, ChannelHandlerContext channelHandlerContext) {
        userConnections.put(userId, channelHandlerContext);
    }

    public void removeUserConnection(String userId) {
        userConnections.remove(userId);
    }

    public ChannelHandlerContext getUserConnection(String userId) {
        return userConnections.get(userId);
    }
}
