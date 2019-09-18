package com.netflix.cloud.user.user.enums;

import lombok.Getter;

/**
 * 定义扣库存异常的enum枚举类型.
 *
 * @author dong
 * @create 2018-10-02 下午5:50
 **/
@Getter
public enum ResultEnum {

    LOGIN_FAIL(1, "用户登录失败"),
    ROLE_ERROR(2, "用户角色权限有误");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
