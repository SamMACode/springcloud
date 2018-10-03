package com.netflix.cloud.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表示购物车中商品的DTO对象
 *
 * @author dong
 * @create 2018-10-02 下午5:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    /**
     * 表示的是商品的id编号.
     * */
    private String productId;

    /**
     * 表示的是购物车中要购买商品的数量.
     * */
    private Integer productQuantity;

}
