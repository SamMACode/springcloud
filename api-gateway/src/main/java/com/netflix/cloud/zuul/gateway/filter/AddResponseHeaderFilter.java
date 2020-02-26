package com.netflix.cloud.zuul.gateway.filter;

import com.netflix.cloud.zuul.gateway.utils.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * 在通过zuul处理完request请求之后设置response响应头信息
 *
 * @author dong
 * @create 2018-10-06 上午11:04
 **/
@Component
public class AddResponseHeaderFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddResponseHeaderFilter.class);

    private static final String ADD_RESPONSE_HEADER = "X-Foo";

    /**
     * 后置过滤器POST_TYPE，在请求结束Response返回之前执行run方法
     * @return
     */
    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        LOGGER.debug("Adding the correlation id to the outbound headers [{}]", FilterUtils.getCorrelationId());

        // 在response响应头中添加所涉及到的header信息
        HttpServletResponse response = requestContext.getResponse();
        response.setHeader(ADD_RESPONSE_HEADER, UUID.randomUUID().toString());

        // 获取原始http请求中传入的关联id，并将它注入到响应中
        requestContext.getResponse().addHeader(FilterUtils.CORRELATION_ID, FilterUtils.getCorrelationId());

        LOGGER.debug("Completing outgoing request for {}", requestContext.getRequest().getRequestURI());
        return null;
    }

}
