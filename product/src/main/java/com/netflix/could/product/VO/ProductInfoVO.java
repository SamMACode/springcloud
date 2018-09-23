package com.netflix.could.product.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 用于封装返回商品信息
 *
 * @author dong
 * @create 2018-09-23 下午10:14
 **/
@Data
public class ProductInfoVO {

    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private Float productPrice;

    @JsonProperty("description")
    private Float productDescription;

    @JsonProperty("icon")
    private String icon;
}
