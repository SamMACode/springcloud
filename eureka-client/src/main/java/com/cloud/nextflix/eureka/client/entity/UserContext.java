package com.cloud.nextflix.eureka.client.entity;

/**
 * @author Sam Ma
 * @date 2020/2/26
 * 将Http首部值存储在UserContext中
 */
public class UserContext {

    /**
     * 经过zuul网关的唯一id编号
     */
    private String correlationId;

    /**
     * gateway授权的token信息
     */
    private String authToken;

    /**
     * 请求中的userId信息
     */
    private String userId;

    /**
     * 请求中的组织机构id
     */
    private String orgId;

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}
