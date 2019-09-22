package com.netflix.cloud.zuul.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 使用zuul进行跨域的设置
 *
 * @author dong
 * @create 2018-10-06 下午10:02
 **/
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        // 设置可跨的域名
        config.setAllowedOrigins(Arrays.asList("*"));
        // 设置header
        config.setAllowedHeaders(Arrays.asList("*"));
        // 设置允许跨域的方法post/get方法都可以
        config.setAllowedMethods(Arrays.asList("*"));
        config.setMaxAge(300L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
