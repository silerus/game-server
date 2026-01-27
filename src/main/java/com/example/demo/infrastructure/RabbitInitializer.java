//package com.example.demo.infrastructure;
//
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RabbitInitializer {
//    private final RabbitTemplate rabbitTemplate;
//
//    public RabbitInitializer(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void init() {
//        rabbitTemplate.execute(channel -> null);
//        System.out.println("RabbitMQ connection established at startup");
//    }
//}
