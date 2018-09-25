package com.netflix.cloud.order.form;

import javax.validation.constraints.NotEmpty;

/**
 * 封装前台传递过来的form表单的值
 *
 * @author dong
 * @create 2018-09-25 下午11:35
 **/
public class OrderForm {

    @NotEmpty(message = "姓名为必输项")
    private String name;

    @NotEmpty(message = "手机号为必输项")
    private String phone;

    @NotEmpty(message = "收货地址为必输项")
    private String address;


}
