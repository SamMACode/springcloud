package com.netflix.cloud.product.exception;

import com.netflix.cloud.product.enums.ResultEnum;

/**
 * 商品的exception
 *
 * @author dong
 * @create 2018-10-02 下午5:49
 **/
public class ProductException extends RuntimeException {

    private Integer code;

    public ProductException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public ProductException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
}
