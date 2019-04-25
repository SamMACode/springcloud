package com.netflix.cloud.product.repository;

import com.netflix.cloud.product.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 操作ProductCategory的数据库操作类
 *
 * @author dong
 * @create 2018-09-23 下午4:56
 **/
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypes);
}
