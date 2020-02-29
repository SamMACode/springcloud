package com.cloud.nextflix.eureka.client.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author Sam Ma
 * @date 2020/2/29
 * 定义接口访问的规则，使它只能由已通过验证的用户访问
 */
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * 所有访问规则都是在覆盖的configure方法中定义
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // anyMatchers()允许开发人员限制对受保护的URL和HTTP DELETE动词的调用
        http.authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/v1/organizations/**")
                .hasAnyRole("ADMIN")
                .anyRequest()
                .authenticated();
    }

}
