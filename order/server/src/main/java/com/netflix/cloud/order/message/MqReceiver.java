package com.netflix.cloud.order.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 用于接收RabbitMQ消息队列消息
 *
 * @author dong
 * @create 2018-10-04 下午10:11
 **/
@Component
@Slf4j
public class MqReceiver {

    // 1.第一种简单的方式是通过 @RabbitListener("myQueue"),不过需要在rabbitMQ控制面板上手动去创建queue.
    //   @RabbitListener("myQueue")
    // 2.第二种是通过 queuesToDeclare注解指明queue的名称.
    //   @RabbitListener(queuesToDeclare = @Queue("myQueue"))
    // 3.第三种方式是自动创建 Exchange和Queue的绑定, 定义exchange与routingKey路由键.
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myQueue"),
            exchange = @Exchange(name = "customExchange"),
            key = "custom"
    ))
    public void process(String message) {
        log.info("消息队列{}接收到的消息内容为: {}", "myQueue", message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("orderComputer"),
            exchange = @Exchange(name = "myOrder"),
            key = "computer"
    ))
    public void processComputerOrder(String message) {
        log.info("数码产品订单Order{}接收到的消息内容为: {}", "orderComputer", message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("orderFruit"),
            exchange = @Exchange(name = "myOrder"),
            key = "fruit"
    ))
    public void processFruitOrder(String message) {
        log.info("水果订单Order{}接收到的消息内容为: {}", "orderFruit", message);
    }

}
