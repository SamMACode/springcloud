package com.netflix.could.product.service;

import com.netflix.could.product.dataobject.ProductCategory;

import java.util.List;

/**
 * 表示商品类别的服务类
 *
 * @author dong
 * @create 2018-09-23 下午9:56
 **/
public interface CategoryService {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypes);
}
