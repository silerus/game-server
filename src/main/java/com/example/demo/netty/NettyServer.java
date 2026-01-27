package com.example.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import jakarta.annotation.PreDestroy;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NettyServer implements SmartLifecycle {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;
    private volatile boolean running = false;

    private final AuthHandler authHandler;
    private final WebSocketHandler webSocketHandler;
    private final InstanceIdentity instanceIdentity;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;

    public NettyServer(AuthHandler authHandler, WebSocketHandler webSocketHandler, InstanceIdentity instanceIdentity, RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin) {
        this.authHandler = authHandler;
        this.webSocketHandler = webSocketHandler;
        this.instanceIdentity = instanceIdentity;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        if (running) return;
        running = true;
        String instanceQueue = "ws-instances." + instanceIdentity.getId();
        Queue queue = new Queue(instanceQueue, false, true, true);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(new DirectExchange("ws-instances")).with(instanceQueue));
        new Thread(() -> {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ChannelPipeline p = ch.pipeline();
                                p.addLast(new HttpServerCodec());
                                p.addLast(new HttpObjectAggregator(65536));
                                p.addLast(authHandler);
                                p.addLast(new WebSocketServerProtocolHandler("/ws"));
                                p.addLast(webSocketHandler);
                            }
                        });
                serverChannel = b.bind(8082).sync().channel();
                System.out.println("Netty started on 8082, instance=" + instanceIdentity.getId());
                serverChannel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stop();
            }
        }, "netty-server").start();

        startRabbitListener(instanceQueue);
    }

    private void startRabbitListener(String instanceQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
        container.setQueueNames(instanceQueue);
        container.setMessageListener(message -> {
            byte[] body = message.getBody();
            this.webSocketHandler.broadcastToClients(body);
        });
        container.start();
    }

    @PreDestroy
    public void stop() {
        System.out.println("Shutting down Netty...");

        if (serverChannel != null) {
            serverChannel.close();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
