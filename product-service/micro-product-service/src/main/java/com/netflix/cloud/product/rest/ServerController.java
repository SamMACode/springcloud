package com.netflix.cloud.product.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于与Order订单服务端进行rest接口测试
 *
 * @author dong
 * @create 2018-10-02 上午9:51
 **/
@RestController
public class ServerController {

    @RequestMapping("/msg")
    public String getMessage() {
        return "this is a product message";
    }
}
