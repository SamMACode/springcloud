package com.netflix.cloud.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Ma
 * @date 2020/2/27
 * oauth-service应用启动入口类，EnableAuthorizationServer用于告诉spring cloud该服务将作为oauth2服务
 */
@SpringBootApplication
@RestController
@EnableResourceServer
@EnableAuthorizationServer
public class Oauth2Application {

    /**
     * 1.http://localhost:8087/oauth/token [post]请求, [form-data]提交表单信息:
     * {
     *      "grant_type": "password",
     *      "scope": "webclient",
     *      "username": "john.carnell",
     *      "password": "password"
     * }
     *
     * 请求返回结果, access_token:是每一个调用都需要提供的验证令牌, token_type表示生成OAuth2访问令牌的类型,
     * refresh_token:当OAuth2访问令牌过期且需要刷新时需要提供的令牌, expires_in:访问令牌过期前的秒数
     * scope:为令牌有效的定义作用域
     * {
     *   "access_token": "3b7e92c2-65e5-438f-aef2-b373c8f41410",
     *   "token_type": "bearer",
     *   "refresh_token": "af44c388-06c5-44d4-89f7-d66de0aedfab",
     *   "expires_in": 43192,
     *   "scope": "webclient"
     * }
     *
     *
     * 2.可以通过/user接口查询用户的基本信息,请求头设置 "Authorization": "Bearer 7c6e8ab3-93db-49fc-a7ca-0ae3739884b0"
     * http://localhost:8087/user [get]
     * {"user": {"password": null, "username": "john.carnell", "authorities": [{"authority": "ROLE_USER"}], "accountNonExpired": true, "accountNonLocked": true}}
     */

    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }

    /**
     * 该服务使用authentication进行授权
     * @param authentication
     * @return
     */
    @RequestMapping(value = {"/user"}, produces = "application/json")
    public Map<String, Object> user(OAuth2Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>(2);
        userInfo.put("user",
                authentication.getUserAuthentication().getPrincipal());
        userInfo.put("authorities",
                AuthorityUtils.authorityListToSet(authentication.getUserAuthentication()
                        .getAuthorities()));
        return userInfo;
    }

}
