package com.rabbitmq.demo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues="test")
public class Receiver {

    @RabbitHandler
    public void process(String key) {
        System.out.println("Receiver:" + key);
    }

}
