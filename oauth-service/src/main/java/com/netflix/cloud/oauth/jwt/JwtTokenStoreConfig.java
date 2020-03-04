package com.netflix.cloud.oauth.jwt;


import com.netflix.cloud.oauth.security.JwtTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author Sam Ma
 * @date 2020/3/4
 * 使用验证服务器以颁发java-web-token令牌,定义了管理jwt令牌的创建、签名和翻译
 */
@Profile("jwt-config-profile")
@Configuration
public class JwtTokenStoreConfig {

    @Autowired
    private ServiceProperties serviceProperties;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 当有多个特定类型的bean,那么就使用被@Primary标注的bean类型进行自动注入
     * @return
     */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        // 用于从出示给服务的令牌中读取数据
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    /**
     * 在jwt和oauth2服务器之间充当翻译,定义了令牌将如何被翻译
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 定义将用于部署令牌的签名秘钥
        converter.setSigningKey(serviceProperties.getSignKey());
        return converter;
    }

    /**
     * 对jwt-token的功能进行加强
     * @return
     */
    @Bean
    public TokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }


}
