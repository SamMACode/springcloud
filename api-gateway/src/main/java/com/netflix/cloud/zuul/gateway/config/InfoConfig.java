package com.netflix.cloud.zuul.gateway.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Sam Ma
 * @date 2020/02/24
 */
@Component
public class InfoConfig implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        String infoJson = "{\"app-name\": \"api-gateway\", \"server.port\": \"8080\"}";
        Map infoMap = JSONObject.parseObject(infoJson, Map.class);
        builder.withDetails(infoMap);
    }

}