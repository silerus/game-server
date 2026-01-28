package com.example.demo.netty;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(byte[] message) {
        try {
            rabbitTemplate.setMandatory(false);
            rabbitTemplate.convertAndSend("", "game-events", message);
        } catch (AmqpException e) {
            // log.debug("Message not sent, queue {} might not exist", routingKey);
        }
    }
}
