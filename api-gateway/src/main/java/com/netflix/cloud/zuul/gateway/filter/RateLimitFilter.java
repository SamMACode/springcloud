package com.netflix.cloud.zuul.gateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.cloud.zuul.gateway.exception.RateLimitException;
import com.netflix.zuul.ZuulFilter;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * 使用google的guava组件进行zuul请求的限流
 *
 * @author dong
 * @create 2018-10-06 上午11:13
 **/
@Component
public class RateLimitFilter extends ZuulFilter {

    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);

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

    @Override
    public Object run() {
        /**
         * 当有请求进来的时候先试着从令牌通中获取访问请求的token.
         * */
        if (!RATE_LIMITER.tryAcquire()) {
            throw new RateLimitException();
        }
        return null;
    }
}
