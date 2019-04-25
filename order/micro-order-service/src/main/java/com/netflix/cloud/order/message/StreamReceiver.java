package com.netflix.cloud.order.message;

import com.netflix.cloud.order.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * 进行接收Spring Cloud Stream的信息
 *
 * @author dong
 * @create 2018-10-04 下午11:36
 **/
@Component
@EnableBinding(StreamClient.class)
@Slf4j
public class StreamReceiver {

    /*@StreamListener("myMessageOutput")
    public void process(Object message) {
        log.info("Stream receiver: {}", message);
    }*/

    /**
     * 用于接收orderDTO类型对象.
     * */
    @StreamListener("myMessageOutput")
    @SendTo("receiveMessageOutput")
    public String process(OrderDTO message) {
        log.info("Stream receiver: {}", message);
        return "received";
    }

    /**
     * 用于消费orderDTO处理之后的结果.
     * */
    @StreamListener("receiveMessageOutput")
    public void process(String message) {
        log.info("result Stream : {}", message);
    }
}
