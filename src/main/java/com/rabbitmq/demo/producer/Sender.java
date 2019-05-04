package com.rabbitmq.demo.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String message = "1111";
        this.rabbitTemplate.convertAndSend("test",message);
    }
}
