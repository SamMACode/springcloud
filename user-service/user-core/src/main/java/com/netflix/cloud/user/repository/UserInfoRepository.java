package com.netflix.cloud.user.repository;

import com.netflix.cloud.user.dataobject.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * 使用JPA操作UserInfo
 *
 * @author dong
 * @create 2018-10-06 下午2:55
 **/
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

    UserInfo findUserInfoByOpenid(String openId);

}
