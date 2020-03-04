package com.netflix.cloud.oauth.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Sam Ma
 * @date 2020/3/4
 * 定义Java-web-token服务配置类
 */
@Profile("jwt-config-profile")
@Configuration
public class ServiceProperties {

    /**
     * jwt中base64进行签名的秘钥key值
     */
    @Value("${jwt.signing-key}")
    private String signKey;

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

}
