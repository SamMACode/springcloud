package com.netflix.cloud.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 使用Rest的方式去调用Product模块的服务
 *
 * @author dong
 * @create 2018-10-02 上午10:02
 **/
@RestController
@Slf4j
public class ClientController {

    /*@Autowired
    private LoadBalancerClient loadBalancerClient;*/

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/getProductMsg")
    public String getProductMsg() {
        /** 1.第一种方式是使用RestTemplate的进行order服务的调用.
         * 缺点:第一种为在restTemplate中的url地址为直接硬编码在代码里的；此外如果服务端order是存在多台服务器的话
         *  也无法进行负载均衡的调用.*/
        /*RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://localhost:8080/msg", String.class);*/

        /**
         * 2.第二种方式: 添加使用LoadBalancerClient组件(通过应用名获取host和Port端口).
         */
        /*RestTemplate restTemplate = new RestTemplate();
        // 使用负载均衡获取到product服务端,动态获取得到其ip以及Port端口号.
        ServiceInstance serviceInstance = loadBalancerClient.choose("product");
        String url = String.format("http://%s:%s", serviceInstance.getHost(), serviceInstance.getPort()) + "/msg";
        String response = restTemplate.getForObject(url, String.class);*/

        /**
         * 3.第三种方式：是前两种的简化方式(使用LoadBalancer注解形式进行处理).
         **/
        String response = restTemplate.getForObject("http://product/msg", String.class);

        log.info("调用Order模块result为: {}", response);
        return response;
    }

}
