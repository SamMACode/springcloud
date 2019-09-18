package com.netflix.cloud.order.service;

import com.netflix.cloud.order.dto.OrderDTO;
import com.netflix.cloud.order.exception.OrderException;

/**
 * 封装调用OrderService生成订单信息的接口
 *
 * @author dong
 * @create 2018-09-25 下午11:08
 **/
public interface OrderService {

    /**
     * 创建新的订单.
     * @param orderTransferObject
     * */
    OrderDTO create(OrderDTO orderTransferObject);

    /**
     * 用于卖家完结新的订单.
     * @param orderId
     * */
    OrderDTO finish(String orderId) throws OrderException;

}
