package com.netflix.could.product.service;

import com.netflix.could.product.dataobject.ProductInfo;

import java.util.List;

/**
 * 商品信息的服务类
 *
 * @author dong
 * @create 2018-09-23 下午9:42
 **/
public interface ProductService {

    public List<ProductInfo> findUpAll();
}
