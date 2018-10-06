package com.netflix.cloud.user.user.service;

import com.netflix.cloud.user.user.dataobject.UserInfo;

/**
 * 定义UserInfo的service服务类
 *
 * @author dong
 * @create 2018-10-06 下午2:58
 **/
public interface UserService {

    /**
     * 通过openid来记性查询用户基本信息.
     * @param openid
     * */
    UserInfo findByOpenid(String openid);
}
