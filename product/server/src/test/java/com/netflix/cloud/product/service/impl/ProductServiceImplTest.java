package com.netflix.cloud.product.service.impl;

import com.netflix.cloud.product.ProductApplicationTests;
import com.netflix.cloud.product.common.DecreaseStockInput;
import com.netflix.cloud.product.common.ProductInfoOutput;
import com.netflix.cloud.product.dataobject.ProductInfo;
import com.netflix.cloud.product.dto.CartDTO;
import com.netflix.cloud.product.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

    @Test
    public void findList() {
        List<ProductInfoOutput> products = this.productService.findList(Arrays.asList("157875196366160022", "157875227953464068"));
        assert products.size() > 0;
    }

    @Test
    public void decreaseStock() {
        DecreaseStockInput cartDTO = new DecreaseStockInput("157875196366160022", 2);
        this.productService.decreaseStock(Arrays.asList(cartDTO));
    }
}