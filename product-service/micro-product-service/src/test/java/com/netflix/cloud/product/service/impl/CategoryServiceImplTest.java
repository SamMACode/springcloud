package com.netflix.cloud.product.service.impl;

import com.netflix.cloud.product.ProductApplicationTests;
import com.netflix.cloud.product.dataobject.ProductCategory;
import com.netflix.cloud.product.service.CategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoryServiceImplTest extends ProductApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void findByCategoryTypeIn() {
        List<ProductCategory> categoryList = categoryService.findByCategoryTypeIn(Arrays.asList(11, 22));
        Assert.assertTrue(categoryList.size() > 0);
    }
}