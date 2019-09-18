package com.netflix.cloud.order.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.netflix.cloud.order.utils.JsonUtil;
import com.netflix.cloud.product.common.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于监听product扣完发送到消息队列内的消息
 *
 * @author dong
 * @create 2018-10-05 下午12:01
 **/
@Component
@Slf4j
public class ProductInfoReceiver {

    private static final String PRODUCT_STOCK_TEMPLATE = "product_stock_%s";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queuesToDeclare = @Queue("productInfo"))
    public void processMessage(String message) {
        // message => productInfo
        List<ProductInfoOutput> productInfoOutputList = (List<ProductInfoOutput>)JsonUtil.fromJson(message,
                new TypeReference<List<ProductInfoOutput>>() {});
        log.info("从消息队列【{}】接收到的消息: {}", "productInfo", productInfoOutputList);

        // 将返回的信息存储到redis中.
        for (ProductInfoOutput productInfoOutput : productInfoOutputList) {
            stringRedisTemplate.opsForValue().set(String.format(PRODUCT_STOCK_TEMPLATE, productInfoOutput.getProductId()),
                    String.valueOf(productInfoOutput.getProductStock()));
        }
    }
}
