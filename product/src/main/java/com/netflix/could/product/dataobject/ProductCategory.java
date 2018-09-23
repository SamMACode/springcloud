package com.netflix.could.product.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * 商品类别信息表
 *
 * @author dong
 * @create 2018-09-23 下午4:51
 **/
@Entity
@Data
public class ProductCategory {

    @Id
    private Integer categoryId;

    /**
     * 商品类别名称.
     * */
    private String categoryName;

    /**
     * 商品类别编号.
     * */
    private Integer categoryType;

    /**
     * 商品类别的创建时间.
     * */
    private Timestamp createTime;

    /**
     * 商品类别的更新时间.
     * */
    private Timestamp updateTime;
}
