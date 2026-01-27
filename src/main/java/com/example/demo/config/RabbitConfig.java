package com.example.demo.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "ws-instances";

    @Bean
    public DirectExchange wsInstancesExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory(
            @Value("${RABBITMQ_HOST}") String host,
            @Value("${RABBITMQ_USER}") String user,
            @Value("${RABBITMQ_PASS}") String pass
    ) {
        CachingConnectionFactory factory = new CachingConnectionFactory(host);
        factory.setUsername(user);
        factory.setPassword(pass);
        factory.setChannelCacheSize(25); // можно больше под WS
        factory.setConnectionTimeout(5000);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
