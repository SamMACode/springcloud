package com.netflix.cloud.product.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Sam Ma
 * @date 2019/09/19
 */
@SpringBootApplication(scanBasePackages = "com.netflix.cloud")
@EnableDiscoveryClient
@EnableBinding(Sink.class)
@EnableJpaRepositories(basePackages = "com.netflix.cloud.product.repository")
@EntityScan(basePackages = "com.netflix.cloud.product.dataobject")
public class ProductApp {

	public static void main(String[] args) {
		SpringApplication.run(ProductApp.class, args);
	}
}
