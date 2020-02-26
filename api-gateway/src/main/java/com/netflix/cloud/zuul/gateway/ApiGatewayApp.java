package com.netflix.cloud.zuul.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author Sam Ma
 * @date 2019/09/22
 * spring cloud微服务api-gateway的应用启动类
 */
@SpringBootApplication
@EnableZuulProxy
public class ApiGatewayApp {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApp.class, args);
	}
}