package com.netflix.cloud.order.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 使用Spring Cloud Stream来整合RabbitMQ使用
 *
 * @author dong
 * @create 2018-10-04 下午11:33
 **/
public interface StreamClient {

    @Input("myMessageInput")
    SubscribableChannel input();

    @Output("myMessageOutput")
    MessageChannel output();

    @Input("receiveMessageInput")
    SubscribableChannel receiveInput();

    @Output("receiveMessageOutput")
    MessageChannel receiveOutput();

}
