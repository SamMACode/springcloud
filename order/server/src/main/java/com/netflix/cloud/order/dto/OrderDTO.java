package com.netflix.cloud.order.dto;

import com.netflix.cloud.order.dataobject.OrderDetail;
import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;

/**
 * 封装Order订单传输对象
 *
 * @author dong
 * @create 2018-09-25 下午11:10
 **/
@Data
public class OrderDTO {

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

    List<OrderDetail> orderDetailList;
}
