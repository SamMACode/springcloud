package com.netflix.cloud.product.enums;

import lombok.Getter;

/**
 * 定义扣库存异常的enum枚举类型.
 *
 * @author dong
 * @create 2018-10-02 下午5:50
 **/
@Getter
public enum ResultEnum {

    PRODUCT_NOT_EXIST(1, "商品不存在"),
    PRODUCT_STOCK_ERROR(2, "商品库存不足"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
