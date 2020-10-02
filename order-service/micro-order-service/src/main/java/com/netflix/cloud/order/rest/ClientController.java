package com.netflix.cloud.order.rest;

import com.netflix.cloud.order.constant.RequestConstInfo;
import com.netflix.cloud.product.client.ProductClient;
import com.netflix.cloud.product.common.DecreaseStockInput;
import com.netflix.cloud.product.common.ProductInfoOutput;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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

    @GetMapping(value = RequestConstInfo.GET_PRODUCT_MSG)
    public String getProductMsg() {
        // 使用feign进行对order服务的调用
        String response = productClient.getProductMsg();
        log.info("调用Order模块result为: {}", response);
        return response;
    }

    /**
     * 获取目前数据库中所有商品列表信息
     *
     * @return
     */
    @HystrixCommand(threadPoolKey = "product-service-threadpool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            },
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
            }
    )
    @GetMapping(value = RequestConstInfo.GET_PRODUCT_LIST)
    public List<ProductInfoOutput> getProductList() {
        List<ProductInfoOutput> response = productClient.listForOrder(Arrays.asList("164103465734242707", "157875196366160022"));
        log.info("response => {}", response);
        return response;
    }

    @GetMapping(value = RequestConstInfo.DECREASE_PRODUCT_STOCK)
    public String decreaseStock() {
        DecreaseStockInput stockInput = new DecreaseStockInput("164103465734242707", 2);
        productClient.decreaseStock(Arrays.asList(stockInput));
        return "ok";
    }

}
