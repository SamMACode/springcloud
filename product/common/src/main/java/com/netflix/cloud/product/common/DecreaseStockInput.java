package com.netflix.cloud.product.common;

import lombok.Data;

/**
 * 减库存入参
 *
 * @author dong
 * @create 2018-10-03 下午2:33
 **/
@Data
public class DecreaseStockInput {

    private String productId;

    private Integer productQuantity;

    public DecreaseStockInput(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
