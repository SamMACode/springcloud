package com.netflix.cloud.user.interceptor;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * @author Sam Ma
 * @date 2020/2/26
 * 定义负载均衡的RestTemplate配置类, 在调用下游接口时将携带参数信息
 */
@Configuration
public class LoadBalancedRestTemplate {

    /**
     * 配置RestTemplate信息，在调用下游服务时将"tmx-correlation-id"和"tmx-auth-token"传递下去
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        // 配置RestTemplate内容, 在其向下游调用服务时通过Interceptor添加header请求头
        if (CollectionUtils.isEmpty(interceptors)) {
            restTemplate.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        } else {
            interceptors.add(new UserContextInterceptor());
            restTemplate.setInterceptors(interceptors);
        }
        return restTemplate;
    }

}
