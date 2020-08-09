package com.netflix.cloud.product.enums;

import lombok.Getter;

/**
 * 表示商品状态的枚举类enum(上下架).
 *
 * @author dong
 * @create 2018-09-23 下午9:46
 **/
@Getter
public enum ProductStatusEnum {

    UP (0, "商品上架"),
    DOWN (0, "商品已下架");

    /**
     * 表示商品status对应的状态码.
     * */
    private Integer code;

    /**
     * 状态描述信息.
     * */
    private String msg;

    ProductStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
