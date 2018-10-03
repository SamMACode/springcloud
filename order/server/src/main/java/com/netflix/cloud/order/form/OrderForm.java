package com.netflix.cloud.order.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 封装前台传递过来的form表单的值
 *
 * @author dong
 * @create 2018-09-25 下午11:35
 **/
@Data
public class OrderForm {

    /**
     * 买家姓名name.
     * */
    @NotEmpty(message = "姓名为必输项")
    private String name;

    /**
     * 买家手机号phone.
     * */
    @NotEmpty(message = "手机号为必输项")
    private String phone;

    /**
     * 买家收货地址address.
     * */
    @NotEmpty(message = "收货地址为必输项")
    private String address;

    /**
     * 买家微信openId.
     * */
    @NotEmpty(message = "openId为必输项")
    private String openId;

    /**
     * 购物车不能为空.
     * */
    @NotEmpty(message = "购物车不能为空")
    private String items;
}
