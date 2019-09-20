package com.netflix.cloud.user.constant;

/**
 * 用户设置cookie常量
 *
 * @author dong
 * @create 2018-10-06 下午3:29
 **/
public interface CookieConstant {

    String TOKEN = "token";

    String OPEN_ID = "openid";

    /**
     * 设置cookie的过期时间。
     * */
    Integer expire = 7200;
}
