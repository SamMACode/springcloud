package com.netflix.cloud.order.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.security.Timestamp;

/**
 * 支付订单明细流水表
 *
 * @author dong
 * @create 2018-09-25 下午10:05
 **/
@Data
@Entity
public class OrderDetail {

    @Id
    private String detailId;

    /**
     * 订单明细所属大订单的id编号.
     * */
    private String orderId;

    /**
     * 该订单明细中需要购买商品的id编号.
     * */
    private String productId;

    /**
     * 该订单明细中商品的名称.
     * */
    private String productName;

    /**
     * 该订单明细中商品的单价.
     * */
    private BigDecimal productPrice;

    /**
     * 该商品对应的商品的购买数量.
     * */
    private Integer productQuantity;

    /**
     * 订单商品明细在页面上显示的小图的大小.
     * */
    private String productIcon;

    private Timestamp createTime;

    private Timestamp updateTime;

}
