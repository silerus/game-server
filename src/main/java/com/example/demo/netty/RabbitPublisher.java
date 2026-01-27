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
