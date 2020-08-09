package com.netflix.cloud.user.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用于基本信息表
 *
 * @author dong
 * @create 2018-10-06 下午2:50
 **/
@Data
@Entity
public class UserInfo {

    @Id
    private String id;

    private String username;

    private String password;

    private String openid;

    private Integer role;

}
