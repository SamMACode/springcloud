package com.netflix.cloud.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 使用前缀的方式获取application.yml文件中的配置
 *
 * @author dong
 * @create 2018-10-04 上午11:45
 **/
@Data
@ConfigurationProperties(prefix = "girl")
@Component
@RefreshScope
public class GirlConfig {

    private String name;

    private Integer age;

}
