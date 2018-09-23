package com.netflix.could.product.service.impl;

import com.netflix.could.product.ProductApplicationTests;
import com.netflix.could.product.dataobject.ProductCategory;
import com.netflix.could.product.service.CategoryService;
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