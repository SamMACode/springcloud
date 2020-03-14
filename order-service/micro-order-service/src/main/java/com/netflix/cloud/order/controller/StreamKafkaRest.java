package com.netflix.cloud.order.controller;


import com.alibaba.fastjson.JSONObject;
import com.netflix.cloud.async.stream.SimpleSourceBean;
import com.netflix.cloud.order.vo.ResultVO;
import com.netflix.cloud.product.common.DecreaseStockInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * @author: Sam Ma
 * @date: 2020/3/13
 * 通过spring cloud stream异步调用减商品库存
 */
@RestController
public class StreamKafkaRest {

    private Logger LOGGER = LoggerFactory.getLogger(StreamKafkaRest.class);

    @Autowired
    private SimpleSourceBean sourceBean;

    /**
     * 通过异步请求的方式进行减库存
     * @return
     */
    @RequestMapping(value = "/decreaseStockAsync", method = RequestMethod.POST)
    public ResultVO decreaseStockAsync(@RequestBody DecreaseStockInput orderParam) {
        LOGGER.info("Start request [{}] , request param orderParam: [{}]", "/decreaseStockAsync",
                JSONObject.toJSONString(orderParam));
        sourceBean.asyncDecreaseStock(Collections.singletonList(orderParam));

        ResultVO resultVo = new ResultVO();
        resultVo.setCode(HttpStatus.OK.value());
        resultVo.setMessage("ok");
        LOGGER.info("Complete request [{}], result: [{}]", "/decreaseStockAsync", JSONObject.toJSONString(resultVo));
        return resultVo;
    }

}
