package com.netflix.cloud.async.stream;

import com.alibaba.fastjson.JSONObject;
import com.netflix.cloud.product.common.DecreaseStockInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Sam Ma
 * @date: 2020/3/13
 * 用于向消息代理发布消息
 */
@Component
public class SimpleSourceBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSourceBean.class);

    private Source source;

    /**
     * Spring Cloud Stream将注入一个Source接口, 以供服务使用
     * @param source
     */
    @Autowired
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    /**
     * 在生成订单后异步调用减库存的接口
     * @param cartList
     */
    public void asyncDecreaseStock(List<DecreaseStockInput> cartList) {
        LOGGER.info("enter SimpleSourceBean.asyncDecreaseStock method, cartList: [{}]",
                JSONObject.toJSONString(cartList));

        // 通过消息中间件将被扣除的订单通过异步的方式进行发送
        cartList.forEach(cartInfo ->
                source.output().send(MessageBuilder.withPayload(cartInfo).build())
        );
    }

}
