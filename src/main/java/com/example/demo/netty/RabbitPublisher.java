package com.example.demo.netty;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;
//    private final DirectExchange exchange;
    private final RedisTemplate<Object, Object> redisTemplate;

    public RabbitPublisher(RabbitTemplate rabbitTemplate, RedisTemplate<Object, Object> redisTemplate) {
        this.rabbitTemplate = rabbitTemplate;
//        this.exchange = exchange;
        this.redisTemplate = redisTemplate;
    }

    public void sendIfQueueExists(String routingKey, byte[] message) {
        try {
            rabbitTemplate.setMandatory(false);
            rabbitTemplate.convertAndSend("", routingKey, message);
        } catch (AmqpException e) {
            // можно логировать на DEBUG, если хочешь
            // log.debug("Message not sent, queue {} might not exist", routingKey);
        }
    }
}
