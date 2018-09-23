package com.netflix.could.product.service.impl;

import com.netflix.could.product.ProductApplicationTests;
import com.netflix.could.product.dataobject.ProductInfo;
import com.netflix.could.product.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImplTest extends ProductApplicationTests {

    @Autowired
    private ProductService productService;

    @Test
    public void findUpAll() {
        List<ProductInfo> products = this.productService.findUpAll();
        Assert.assertTrue(products.size() > 0);
    }
}