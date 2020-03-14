package com.netflix.cloud.product.async.stream;


import com.alibaba.fastjson.JSONObject;
import com.netflix.cloud.product.common.DecreaseStockInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

/**
 * @author Sam Ma
 * @date 2020/3/13
 * Spring cloud stream用于接收从kafka中的消息
 */
@Component
public class StreamSinkInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamSinkInput.class);

    /**
     * 每当收到来自input通道的消息是,spring cloud stream都会执行该方法
     * @param stockInput
     */
    @StreamListener(Sink.INPUT)
    public void logDecreaseRequest(DecreaseStockInput stockInput) {
        LOGGER.info("Received an event from order-service, stockInput: [{}]", JSONObject.toJSONString(stockInput));
    }

}
