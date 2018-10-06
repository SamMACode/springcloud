package com.netflix.cloud.user.user.repository;

import com.netflix.cloud.user.user.dataobject.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 使用JPA操作UserInfo
 *
 * @author dong
 * @create 2018-10-06 下午2:55
 **/
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

    UserInfo findUserInfoByOpenid(String openId);

}
