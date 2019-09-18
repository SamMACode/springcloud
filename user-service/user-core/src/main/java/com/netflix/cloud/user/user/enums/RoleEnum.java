package com.netflix.cloud.user.user.enums;

import lombok.Getter;

/**
 * 定义系统用户角色的enum类
 *
 * @author dong
 * @create 2018-10-06 下午3:19
 **/
@Getter
public enum RoleEnum {

    BUYER(1, "卖家用户"),
    SELLER(2, "卖家用户"),
    ;

    private Integer code;

    private String message;

    RoleEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
