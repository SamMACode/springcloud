package com.netflix.cloud.oauth.jwt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

/**
 * @author Sam Ma
 * @date 2020/3/4
 * 使用JwtOauth2Config类将jwt挂钩到验证服务中
 */
@Profile("jwt-config-profile")
@Configuration
public class JwtOauth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * example: 使用java-web-token进行授权,在application.yml中指定配置: spring.profiles.active: jwt-config-profile
     * [post] http://localhost:8087/oauth/token
     * {
     *      "grant_type": "password",
     *      "scope": "webclient",
     *      "username": "john.carnell",
     *      "password": "password"
     * }
     *
     * 返回数据内容:
     * {
     *   "access_token": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODMzODEzNzQsInVzZXJfbmFtZSI6ImpvaG4uY2FybmVsbCIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIzMWM3YzM1MC03MDA0LTRmOTEtOTZiMC1kY2Y2YjRlOWEzMzciLCJjbGllbnRfaWQiOiJjcm93biIsInNjb3BlIjpbIndlYmNsaWVudCJdfQ.fI1cSmey2kqUQIyYbyEG9HA2BTUNBkZSUVrrXSwydD8",
     *   "token_type": "bearer",
     *   "refresh_token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX25hbWUiOiJqb2huLmNhcm5lbGwiLCJzY29wZSI6WyJ3ZWJjbGllbnQiXSwiYXRpIjoiMzFjN2MzNTAtNzAwNC00ZjkxLTk2YjAtZGNmNmI0ZTlhMzM3IiwiZXhwIjoxNTg1OTMwMTc0LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiMWZmZjE5ODItODg4ZC00MzJiLTgzYzctMmQ3NDMwNjI2YjliIiwiY2xpZW50X2lkIjoiY3Jvd24ifQ.FrX-o-cYITu0mvAl2l4KduxcVFuns611rvwA5rYD2jc",
     *   "expires_in": 43199,
     *   "scope": "webclient",
     *   "jti": "31c7c350-7004-4f91-96b0-dcf6b4e9a337"
     * }
     */
    @Autowired
    private TokenEnhancer jwtTokenEnhancer;

    /**
     * 使用AuthorizationServerEndpoints进行配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer, jwtAccessTokenConverter));

        // 此配置为钩子,用于告诉Spring Security OAuth2代码使用Jwt
        endpoints
                .tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    /**
     * 定义那些客户端将注册到服务
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("crown")
                .secret("{noop}thisissecret")
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("webclient", "mobileclient");
    }

}
