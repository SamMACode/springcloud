package com.netflix.cloud.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于验证从spring cloud config加载的配置文件是否正常
 *
 * @author dong
 * @create 2018-10-04 上午10:35
 **/
@RestController
@RequestMapping("/env")
public class EnvController {

    @Value("${env}")
    private String enviroment;

    @GetMapping("/print")
    public String printEnv() {
        return enviroment;
    }
}
