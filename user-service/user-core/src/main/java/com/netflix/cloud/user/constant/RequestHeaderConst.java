package com.netflix.cloud.user.constant;

/**
 * @author Sam Ma
 * @date 2020/08/08
 * 用于获取request请求头中的相关属性
 */
public class RequestHeaderConst {

    public static final String CORRELATION_ID = "tmx-correlation-id";

    /**
     * 授权的token信息
     */
    public static final String AUTH_TOKEN = "tmx-auth-token";

    /**
     * 用户的id相关信息
     */
    public static final String USER_ID = "tmx-user-id";

    /**
     * 请求的组织机构id编号
     */
    public static final String ORG_ID = "tmx-org-id";

    private RequestHeaderConst() {
    }

}
