package com.netflix.cloud.user.user.controller;

import com.netflix.cloud.user.user.vo.ResultVO;
import com.netflix.cloud.user.user.constant.CookieConstant;
import com.netflix.cloud.user.user.constant.RedisConstant;
import com.netflix.cloud.user.user.dataobject.UserInfo;
import com.netflix.cloud.user.user.enums.ResultEnum;
import com.netflix.cloud.user.user.enums.RoleEnum;
import com.netflix.cloud.user.user.service.UserService;
import com.netflix.cloud.user.user.utils.CookieUtil;
import com.netflix.cloud.user.user.utils.ResultVOUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户进行登录的Controller
 *
 * @author dong
 * @create 2018-10-06 下午3:05
 **/
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 用于处理买家登录的请求.
     * @param openId
     * @param response
     * @return
     * */
    @GetMapping("/buyer")
    public ResultVO buyer(@RequestParam("openid")String openId, HttpServletResponse response) {
        // 1.根据openid去数据库中查询对应的用户是否存在.
        UserInfo userInfo = userService.findByOpenid(openId);
        if(null == userInfo) {
            return ResultVOUtil.error(ResultEnum.LOGIN_FAIL);
        }
        // 2.判断当前登录/buyer接口的用户角色是否正确.
        if(!RoleEnum.BUYER.getCode().equals(userInfo.getRole())) {
            return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
        }
        // 3.在cookie中设置openid
        CookieUtil.set(response, CookieConstant.OPEN_ID, openId, CookieConstant.expire);
        return ResultVOUtil.success();
    }

    /**
     * 用于处理卖家登录的请求.
     * @param openId
     * @param response
     * @return
     * */
    @GetMapping("/seller")
    public ResultVO seller(@RequestParam("openid")String openId, HttpServletResponse response, HttpServletRequest request) {
        //判断是否已经登录(cookie不为空并且redis中存储有cookie对应的值),避免每次请求都往Cookie中放置新的值.
        Cookie cookie = CookieUtil.getCookie(request, CookieConstant.TOKEN);
        if(cookie != null && !StringUtils.isEmpty(
                redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE, cookie.getValue())))) {
            return ResultVOUtil.success();
        }

        // 1.根据openid去数据库中查询对应的用户是否存在.
        UserInfo userInfo = userService.findByOpenid(openId);
        if(null == userInfo) {
            return ResultVOUtil.error(ResultEnum.LOGIN_FAIL);
        }

        // 2.判断当前登录/buyer接口的用户角色是否正确.
        if(!RoleEnum.SELLER.getCode().equals(userInfo.getRole())) {
            return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
        }

        // 3.在redis中设置key=uuid,value=xyz.
        String uuidToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_TEMPLATE, uuidToken),
                openId, CookieConstant.expire, TimeUnit.SECONDS);

        // 4.在cookie中设置openid
        CookieUtil.set(response, CookieConstant.TOKEN, uuidToken, CookieConstant.expire);
        return ResultVOUtil.success();
    }
}
