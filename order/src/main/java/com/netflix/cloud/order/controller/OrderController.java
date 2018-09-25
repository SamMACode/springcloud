package com.netflix.cloud.order.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * 订单的Controller实体类
 *
 * @author dong
 * @create 2018-09-25 下午10:56
 **/
@RestController
public class OrderController {

    /**
     * 1.第一步对ajax传递来的参数进行校验.
     * 2.查询商品服务(调用product服务接口).
     * 3.计算Order订单中的商品总价.
     * 4.扣库存(调用product服务接口).
     * 5.在order部分生成订单流水信息(master表和detail表).
     * */
    public void create() {

    }

}
