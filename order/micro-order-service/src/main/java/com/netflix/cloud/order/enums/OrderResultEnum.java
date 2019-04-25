package com.netflix.cloud.order.enums;

import lombok.Getter;

/**
 * 表示对参数校验之后的结果枚举
 *
 * @author dong
 * @create 2018-09-26 下午10:03
 **/
@Getter
public enum OrderResultEnum {

    PARAM_ERROR(1, "参数错误"),
    CART_EMPTY(2, "购物车为空"),
    ORDER_NOT_EXIST(3, "订单不存在"),
    ORDER_STATUS_ERROR(4, "订单状态错误"),
    ORDER_DETAILS_IS_EMPTY(5, "订单明细为空"),
    ;

    /**
     * 表示的是异常对应的错误码信息.
     * */
    private Integer code;
    /**
     * 表示的是异常对应的错误描述信息message.
     * */
    private String message;

    OrderResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
