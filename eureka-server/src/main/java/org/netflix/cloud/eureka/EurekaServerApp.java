package org.netflix.cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Sam Ma
 * @date 2019/9/18
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp {

	/*
	 * 使用kubectl port-forward eureka-server-7b88f67bc5-mn5g9 8762:8762 --namespace svc服务转发请求
	 * 打开node-port命令: minikube service eureka-server -n svc
	 */
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApp.class, args);
	}
}
