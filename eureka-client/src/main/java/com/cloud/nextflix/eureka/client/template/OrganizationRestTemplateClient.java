package com.cloud.nextflix.eureka.client.template;

import com.cloud.nextflix.eureka.client.filter.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @author Sam Ma
 * @date 2020/3/3
 * 使用OAuth2RestTemplate来传播OAuth2访问令牌(代码未生效)
 */
@Component
public class OrganizationRestTemplateClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

//    @Autowired
    private OAuth2RestTemplate restTemplate;

    /**
     * 通过RestTemplate使用组织机构Id调用组织机构服务
     * @param organizationId
     * @return
     */
    public Map getOrganization(String organizationId) {
        LOGGER.debug("In licensing service.getOrganization correlationId: [{}]",
                UserContextHolder.getContext().getCorrelationId());

        ResponseEntity<Map> response = restTemplate.exchange("http://zuulserver:5555/api/organization/v1/organizations/{organizationId}",
                HttpMethod.GET,
                null, Map.class, organizationId);
        return response.getBody();
    }

}
