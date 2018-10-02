package com.netflix.cloud.product.repository;

import com.netflix.cloud.product.dataobject.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findByCategoryTypeIn() {
        List<ProductCategory> productCategoryList = productCategoryRepository.findByCategoryTypeIn(Arrays.asList(11, 22));
        assert productCategoryList.size() > 0;
    }
}