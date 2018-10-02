package com.netflix.cloud.order.client;

import com.netflix.cloud.order.dataobject.ProductInfo;
import com.netflix.cloud.order.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 封装调用product服务的接口
 *
 * @author dong
 * @create 2018-10-02 上午11:55
 **/
@Component
@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping("/msg")
    String getProductMsg();

    @PostMapping("/product/listForOrder")
    List<ProductInfo> listForOrder(@RequestBody List<String> productIdList);

    @PostMapping("/product/decreaseStock")
    void decreaseStock(@RequestBody List<CartDTO> cartList);
}
