package com.netflix.could.product.service.impl;

import com.netflix.could.product.dataobject.ProductInfo;
import com.netflix.could.product.repository.ProductInfoRepository;
import com.netflix.could.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务的业务实现类
 *
 * @author dong
 * @create 2018-09-23 下午9:43
 **/
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public List<ProductInfo> findUpAll() {
        return this.productInfoRepository.findProductInfoByProductStatus(0);
    }
}
