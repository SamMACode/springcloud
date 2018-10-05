package com.netflix.cloud.order.controller;

import com.netflix.cloud.order.dto.OrderDTO;
import com.netflix.cloud.order.message.StreamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

/**
 * 用于发送消息的Controller
 *
 * @author dong
 * @create 2018-10-04 下午11:39
 **/
@RestController
public class SenderMessageController {

    @Autowired
    private StreamClient streamClient;

/*    @GetMapping("/sendMessage")
    public void process() {
        String message = "now " + new Date();
        streamClient.output().send(MessageBuilder.withPayload(message).build());
    }*/

    /**
     * 发送的消息类型为Order对象.
     * */
    @GetMapping("/sendMessage")
    public void process() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("1234589");
        streamClient.output().send(MessageBuilder.withPayload(orderDTO).build());
    }
}
