package com.cloud.nextflix.eureka.client.constant;

/**
 * @author Sam Ma
 * @date 2020/2/26
 * 请求经过Zuul的请求头信息
 */
public interface ZuulFilterConstant {

    String CORRELATION_ID = "tmx-correlation-id";

    /**
     * 授权的token信息
     */
    String AUTH_TOKEN = "tmx-auth-token";

    /**
     * 用户的id相关信息
     */
    String USER_ID = "tmx-user-id";

    /**
     * 请求的组织机构id编号
     */
    String ORG_ID = "tmx-org-id";

}
