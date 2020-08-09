package com.netflix.cloud.order.rest;

import com.netflix.cloud.order.constant.RequestConstInfo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于获取动态的配置更新
 *
 * @author dong
 * @create 2018-10-04 上午11:48
 **/
@RestController
public class DynamicRefreshConfig {

    @Autowired
    private GirlConfig girlConfig;

    @GetMapping(value = RequestConstInfo.DYNAMIC_REFRESH_SCOPE)
    public String getGrilConfiguration() {
        return girlConfig.getName() + ", " + girlConfig.getAge();
    }

    /**
     * 使用前缀的方式获取application.yml文件中的配置
     */
    @Data
    @ConfigurationProperties(prefix = "girl")
    @Component
    @RefreshScope
    class GirlConfig {
        private String name;
        private Integer age;
    }

}
