package com.example.demo.actor;

import io.netty.channel.Channel;

public class Connect implements GameCommand {
    private final Channel channel;

    public Connect(Channel channel) {
        this.channel = channel;
    }

    public Channel channel() {
        return channel;
    }
}
