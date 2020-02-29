package com.netflix.cloud.zuul.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * @author Sam Ma
 * @date 2018/10/06
 * 经过zuul的请求必须都携带token参数 (代码临时被注掉,目前通过网关不校验api-token)
 **/
//@Component
public class TokenPreFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenPreFilter.class);

    /**
     * 设置token的filter过滤器
     */
    private static final String TOKEN_KEY = "token";

    /**
     * 定义/oauth/token请求的url地址
     */
    private static final String OAUTH2_TOKEN_URL = "/oauth/token";

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

        if (request.getRequestURI().contains(OAUTH2_TOKEN_URL)) {
            LOGGER.info("request url [{}] is allowed", request.getRequestURI());
        }

        // 可以从url中获取cookie参数,也可以从cookie或header中获取参数信息
        String token = request.getParameter(TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }

}
