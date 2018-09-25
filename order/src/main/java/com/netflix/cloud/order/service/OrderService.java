package com.netflix.cloud.order.service;

import com.netflix.cloud.order.dto.OrderDTO;

/**
 * 封装调用OrderService生成订单信息的接口
 *
 * @author dong
 * @create 2018-09-25 下午11:08
 **/
public interface OrderService {

    public OrderDTO create(OrderDTO orderTransferObject);

}
