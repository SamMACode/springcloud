package com.netflix.cloud.order.enums;

import lombok.Getter;

/**
 * 表示订单支付状态的枚举
 *
 * @author dong
 * @create 2018-09-25 下午10:29
 **/
@Getter
public enum PayStatusEnum {

    WAITING (0, "等待支付"),
    FINISHED (1, "订单已完成支付"),
    ;

    private Integer code;

    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
