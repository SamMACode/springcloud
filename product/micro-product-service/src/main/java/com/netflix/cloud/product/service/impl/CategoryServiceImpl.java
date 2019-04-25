package com.netflix.cloud.product.service.impl;

import com.netflix.cloud.product.dataobject.ProductCategory;
import com.netflix.cloud.product.repository.ProductCategoryRepository;
import com.netflix.cloud.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表示商品类别的业务实现类
 *
 * @author dong
 * @create 2018-09-23 下午9:58
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypes) {
        return categoryRepository.findByCategoryTypeIn(categoryTypes);
    }
}
