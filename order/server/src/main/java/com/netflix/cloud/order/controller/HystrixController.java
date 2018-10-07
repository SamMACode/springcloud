package com.netflix.cloud.order.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * 使用Hystrix模拟服务降级
 * @DefaultProperties 表示默认的提示方法,当服务不可用的时候回调用默认方法 defaultFallback.
 *
 * @author dong
 * @create 2018-10-07 上午10:19
 **/
@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {

    /**
     * // throw new RuntimeException("服务发生异常了"); 也可以使用自定义异常触发服务降级.
     * execution.isolation.thread.timeoutInMilliseconds
     * 该属性可以从来设置hystrix调用的超时时间(在HystrixCommandProperties文件里).
     * */
    @HystrixCommand(commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    @GetMapping("getProductInfoList")
    public String getProductInfoList() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://localhost:8085/product/listForOrder",
                Arrays.asList("157875196366160022"),
                String.class);
    }

    private String fallback() {
        return "太拥挤了，请稍后重试~~";
    }

    private String defaultFallback() {
        return "默认提示: 太拥挤了，请稍后重试~~";
    }
}
