package com.netflix.cloud.order.rest;

import com.netflix.cloud.order.constant.RequestConstInfo;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * 使用Hystrix模拟服务降级
 * @see com.netflix.hystrix.contrib.javanica.annotation#DefaultProperties: 表示默认的提示方法,
 *      当服务不可用的时候回调用默认方法 defaultFallback
 * @author dong
 * @create 2018-10-07 上午10:19
 **/
@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {

    /**
     * // throw new RuntimeException("服务发生异常了"); 也可以使用自定义异常触发服务降级
     * execution.isolation.thread.timeoutInMilliseconds
     * 该属性可以从来设置hystrix调用的超时时间(在HystrixCommandProperties文件里)
     * */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),     // 设置熔断是否开启
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),   // 表示在滚动期断路器的最小请求数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),  // 设置熔断器时间窗的时间
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),    // 表示打开熔断器的百分比(当调用失败达到60%以上时候)
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    }, fallbackMethod = "fallback")
    @GetMapping(value = RequestConstInfo.HYSTRIX_GET_PRODUCT_LIST)
    public String getProductInfoList(@RequestParam("number") Integer number) {
        if(number % 2 == 0) {
            return "success";
        }
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://localhost:8083/product/listForOrder",
                Arrays.asList("157875196366160022"),
                String.class);
    }

    private String fallback(Integer number) {
        return "太拥挤了，请稍后重试~~";
    }

    private String defaultFallback(Integer number) {
        return "默认提示: 太拥挤了，请稍后重试~~";
    }
}
