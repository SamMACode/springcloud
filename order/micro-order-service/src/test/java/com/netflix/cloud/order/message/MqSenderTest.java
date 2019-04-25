package com.netflix.cloud.order.message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * MQ发送消息的测试类
 *
 * @author dong
 * @create 2018-10-04 下午10:17
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class MqSenderTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void testSendMessageToCustomMQ() {
        amqpTemplate.convertAndSend("customExchange", "custom","hello spring boot rabbitMQ");
    }

    @Test
    public void testSendMessageToComputerMQ() {
        amqpTemplate.convertAndSend("myOrder", "computer","i want a mac computer.");
    }

    @Test
    public void testSendMessageToFruitMQ() {
        amqpTemplate.convertAndSend("myOrder", "fruit","i want to buy some apple.");
    }
}
