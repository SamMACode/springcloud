package com.netflix.cloud.user.service.impl;

import com.netflix.cloud.user.dataobject.UserInfo;
import com.netflix.cloud.user.repository.UserInfoRepository;
import com.netflix.cloud.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 实现用户访问UserInfo接口的服务
 *
 * @author dong
 * @create 2018-10-06 下午3:00
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo findByOpenid(String openid) {
        return userInfoRepository.findUserInfoByOpenid(openid);
    }
}
