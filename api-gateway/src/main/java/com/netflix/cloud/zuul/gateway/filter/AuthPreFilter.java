package com.netflix.cloud.zuul.gateway.filter;

import com.netflix.cloud.zuul.gateway.constant.CookieConstant;
import com.netflix.cloud.zuul.gateway.constant.RedisConstant;
import com.netflix.cloud.zuul.gateway.utils.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * 经过zuul的请求必须都携带token参数
 *
 * @author dong
 * @create 2018-10-06 上午10:49
 **/
@Component
public class AuthPreFilter extends ZuulFilter {

    private static final String ORDER_CREATE_URL = "/order/order/create";

    private static final String ORDER_FINISH_URL = "/order/order/finish";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 前置过滤器,在请求到达路由过滤器调用run方法
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 在run方法中是实现真正的过滤请求的业务逻辑.
     * */
    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        /*
         * order/create 只能买家登录(cookie里有openid)
         * order/finish 只能卖家登录(cookie里有token,并且对应的redis中有值)
         * order/list 都可以访问
         */
        if(ORDER_CREATE_URL.equals(request.getRequestURI())) {
            Cookie cookie = CookieUtil.getCookie(request, CookieConstant.OPEN_ID);
            if(cookie == null || StringUtils.isEmpty(cookie.getValue())) {
                requestContext.setSendZuulResponse(false);
                requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            }
        }

        // "order/finish"只能卖家进行访问该url地址.
        if(ORDER_FINISH_URL.equals(request.getRequestURI())) {
            Cookie uuidToken = CookieUtil.getCookie(request, CookieConstant.TOKEN);
            if(uuidToken == null || uuidToken.getValue() == null
                    || StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE, uuidToken)))) {
                requestContext.setSendZuulResponse(false);
                requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            }
        }
        return null;
    }

}
