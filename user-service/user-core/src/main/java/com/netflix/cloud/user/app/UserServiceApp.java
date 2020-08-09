package com.netflix.cloud.user.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Sam Ma
 * @date 2019/09/19
 */
@SpringBootApplication(scanBasePackages = "com.netflix.cloud")
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.netflix.cloud.user.repository")
@EntityScan(basePackages = "com.netflix.cloud.user.dataobject")
public class UserServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApp.class, args);
	}
}
