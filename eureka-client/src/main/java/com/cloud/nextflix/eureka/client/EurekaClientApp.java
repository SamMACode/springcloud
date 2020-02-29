package com.cloud.nextflix.eureka.client;

import com.cloud.nextflix.eureka.client.filter.UserContextFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Sam Ma
 * @date 2019/09/18
 * 定义Eureka Client服务用于与Oauth2服务联调，对访问接口根据角色Role进行控制
 */
@RestController
@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
public class EurekaClientApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(EurekaClientApp.class);

	/**
	 * 定义删除的Url地址
	 */
	private static final String DELETE_URL = "/v1/organizations/delete";

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientApp.class, args);
	}

	/**
	 * 定义过滤器Filter, 该过滤器会拦截对该服务的所有传入调用, 检查传入调用的Http首部中是否存在Oauth2令牌
	 * @return
	 */
	@Bean
	public Filter userContextFilter() {
		UserContextFilter userContextFilter = new UserContextFilter();
		return userContextFilter;
	}

	/**
	 * 定义delete方法,在接收到从网关的请求时将参数打印出来
	 * @param deleteId
	 * @return
	 */
	@RequestMapping(value = DELETE_URL, method = RequestMethod.DELETE)
	public Map<String, Object> getOrganizationInfo(@RequestBody String deleteId) {
		LOGGER.info("Start request [{}], request param deleteId: [{}]", DELETE_URL, deleteId);
		Map<String, Object> resultMap = new HashMap<>(2);
		resultMap.put("code", HttpStatus.OK.value());
		resultMap.put("msg", "ok");
		return resultMap;
	}


}
