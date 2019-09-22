package com.netflix.cloud.zuul.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Component;

/**
 * zuul配置的动态注入
 *
 * @author dong
 * @create 2018-10-06 上午10:42
 **/
@Component
public class ZuulConfig {

    @RefreshScope
    @ConfigurationProperties("zuul")
    public ZuulProperties zuulProperties() {
        return new ZuulProperties();
    }

}