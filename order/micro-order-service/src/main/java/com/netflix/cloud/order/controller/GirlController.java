package com.netflix.cloud.order.controller;

import com.netflix.cloud.order.config.GirlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于获取动态的配置更新
 *
 * @author dong
 * @create 2018-10-04 上午11:48
 **/
@RestController
public class GirlController {

    @Autowired
    private GirlConfig girlConfig;

    @GetMapping("/gril/refreshconfig")
    public String getGrilConfiguration() {
        return girlConfig.getName() + ", " + girlConfig.getAge();
    }
}
