package com.netflix.cloud.product.repository;

import com.netflix.cloud.product.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 操作商品基本信息的repository
 *
 * @author dong
 * @create 2018-09-23 下午5:23
 **/
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {

    public List<ProductInfo> findProductInfoByProductStatus(Integer status);

    public List<ProductInfo> findByProductIdIn(List<String> productList);
}
