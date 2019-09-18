package com.netflix.cloud.order.exception;

import com.netflix.cloud.order.enums.OrderResultEnum;

/**
 * 订单的异常类定义
 *
 * @author dong
 * @create 2018-09-26 下午9:56
 **/
public class OrderException extends Exception {

    private Integer code;

    public OrderException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public OrderException(OrderResultEnum orderResultEnum) {
        super(orderResultEnum.getMessage());
        this.code = orderResultEnum.getCode();
    }
}
