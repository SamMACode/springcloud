package com.netflix.cloud.zuul.gateway.filter;

import com.netflix.cloud.zuul.gateway.utils.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author Sam Ma
 * @date 2020/2/25
 * 构建第一个生成关联Id的Zuul前置过滤器
 */
@Component
public class TrackingFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingFilter.class);

    private static final int FILTER_ORDER = 1;

    private static final boolean SHOULD_FILTER = true;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * filterOrder方法返回一个整数值,指示不同类型的过滤器的执行顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    /**
     * 是否应该对经过zuul的请求进行过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    /**
     * 当前request的header中是否存在correlation-id请求信息
     * @return
     */
    private boolean isCorrelationIdPresent() {
        if (null != FilterUtils.getCorrelationId()) {
            return true;
        }
        return false;
    }

    /**
     * 从UUID工具类中生成随机字符串
     * @return
     */
    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * 过滤器中真正的业务逻辑代码
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        if (isCorrelationIdPresent()) {
            LOGGER.debug("tmx-correlation-id found in tracking filter: [{}]", FilterUtils.getCorrelationId());
        } else {
            // 否则在每个经过zuul的请求头中添加上"tmx-correlation-id"
            FilterUtils.setCorrelationId(generateCorrelationId());
            LOGGER.debug("tmx-correlation-id generated in tracking filter: [{}]", FilterUtils.getCorrelationId());
        }
        RequestContext ctx = RequestContext.getCurrentContext();
        LOGGER.debug("Processing incoming request for [{}]", ctx.getRequest().getRequestURI());
        return null;
    }


}
