package com.netflix.cloud.order.controller;

import com.netflix.cloud.product.client.ProductClient;
import com.netflix.cloud.product.common.DecreaseStockInput;
import com.netflix.cloud.product.common.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 使用Rest的方式去调用Product模块的服务
 *
 * @author dong
 * @create 2018-10-02 上午10:02
 **/
@RestController
@Slf4j
public class ClientController {

    @Autowired
    private ProductClient productClient;

    @GetMapping("/getProductMsg")
    public String getProductMsg() {
        // 使用feign进行对order服务的调用.
        String response = productClient.getProductMsg();
        log.info("调用Order模块result为: {}", response);
        return response;
    }

    @GetMapping("/getProductList")
    public String getProductList() {
        List<ProductInfoOutput> response = productClient.listForOrder(Arrays.asList("157875227953464068"));
        log.info("response => {}", response);
        return "ok";
    }

    @GetMapping("/productDecreaseStock")
    public String decreaseStock() {
        DecreaseStockInput stockInput = new DecreaseStockInput("164103465734242707", 2);
        productClient.decreaseStock(Arrays.asList(stockInput));
        return "ok";
    }

}
