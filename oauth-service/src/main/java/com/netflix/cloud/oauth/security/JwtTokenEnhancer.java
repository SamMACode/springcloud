package com.netflix.cloud.oauth.security;


import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Ma
 * @date 2020/3/4
 * JwtTokenEnhancer类
 */
public class JwtTokenEnhancer implements TokenEnhancer {

    /**
     * 对jwt中的accessToken数据添加额外的内容
     * @param accessToken
     * @param oAuth2Authentication
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> additionalInfo = new HashMap<>(1);
        additionalInfo.put("organizationId", "org-value");
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}
