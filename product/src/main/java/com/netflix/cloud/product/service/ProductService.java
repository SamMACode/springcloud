package com.netflix.cloud.product.service;

import com.netflix.cloud.product.dataobject.ProductInfo;
import com.netflix.cloud.product.dto.CartDTO;

import java.util.List;

/**
 * 商品信息的服务类
 *
 * @author dong
 * @create 2018-09-23 下午9:42
 **/
public interface ProductService {

    /**
     * 查询所有在线的商品信息.
     * */
    List<ProductInfo> findUpAll();

    /**
     * 根据商品id查询商品详细信息ProductInfo.
     * @param productList
     * @return
     * */
    List<ProductInfo> findList(List<String> productList);

    /**
     * 根据订单中商品列表进行扣库存.
     * @param cartList
     * */
    void decreaseStock(List<CartDTO> cartList);
}
