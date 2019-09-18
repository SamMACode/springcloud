package com.netflix.cloud.order.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.security.Timestamp;

/**
 * 用户支付订单主表
 *
 * @author dong
 * @create 2018-09-25 下午10:00
 **/
@Data
@Entity
public class OrderMaster {

    @Id
    private String orderId;

    /**
     * buyer买家姓名
     * */
    private String buyerName;

    /**
     * 买家在系统的预留手机号.
     * */
    private String buyerPhone;

    /**
     * 买家的收货地址.
     * */
    private String buyerAddress;

    /**
     * 微信用户的openId信息.
     * */
    private String buyerOpenid;

    /**
     * 该笔订单的总金额orderAmount.
     * */
    private BigDecimal orderAmount;

    /**
     * 该笔订单的状态.
     * */
    private Integer orderStatus;

    /**
     * 订单的支付状态:未支付/已经支付成功.
     * */
    private Integer payStatus;

    private Timestamp createTime;

    private Timestamp updateTime;
}
