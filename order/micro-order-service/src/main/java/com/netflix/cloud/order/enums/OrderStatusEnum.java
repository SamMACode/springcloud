package com.netflix.cloud.order.enums;

import lombok.Getter;

/**
 * 表示订单状态枚举
 *
 * @author dong
 * @create 2018-09-25 下午10:25
 **/
@Getter
public enum OrderStatusEnum {

    NEW (0, "新订单"),
    FINISHED (1, "订单已完结"),
    CANCELED (2, "订单已取消"),
    ;

    private Integer orderStatus;

    private String orderDesc;

    OrderStatusEnum(Integer orderStatus, String orderDesc) {
        this.orderStatus = orderStatus;
        this.orderDesc = orderDesc;
    }
}
