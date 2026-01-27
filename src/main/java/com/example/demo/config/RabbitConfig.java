package com.example.demo.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "game-exchange";

    @Value("${RABBITMQ_USER}")
    private String userName;
    @Value("${RABBITMQ_PASS}")
    private String password;
    @Value("${RABBITMQ_HOST}")
    private String host;

    @Bean
    public DirectExchange gameExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false); // durable=true, autoDelete=false
    }

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(host);
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setChannelCacheSize(10);
        factory.createConnection();
        return factory;
    }
}
