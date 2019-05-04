package com.rabbitmq.demo;

import com.rabbitmq.demo.producer.Sender;
import org.springframework.beans.factory.annotation.Autowired;


public class Test {

    @Autowired
    private Sender sender;

    public void hello() {
        sender.send();
    }

}
