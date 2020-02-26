package com.netflix.cloud.zuul.gateway.utils;

import com.netflix.zuul.context.RequestContext;

/**
 * @author Sam Ma
 * @date 2020/2/25
 * 过滤器的工具类
 */
public class FilterUtils {

    public static final String CORRELATION_ID = "tmx-correlation-id";

    public static final String SERVICE_ID = "serviceId";

    /**
     * 从RequestContext中获取"tmx-correlation-id"信息
     * @return
     */
    public static String getCorrelationId() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (null != ctx.getRequest().getHeader(CORRELATION_ID)) {
            return ctx.getRequest().getHeader(CORRELATION_ID);
        } else {
            return ctx.getZuulRequestHeaders().get(CORRELATION_ID);
        }
    }

    /**
     * 在zuul的请求头中设置"tmx-correlation-id"信息
     * @param correlationId
     */
    public static void setCorrelationId(String correlationId) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(CORRELATION_ID, correlationId);
    }

    /**
     * 从RequestContext中获取serviceId信息
     * @return
     */
    public static String getServiceId() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (null == ctx.get(SERVICE_ID)) {
            return "";
        }
        return ctx.get(SERVICE_ID).toString();
    }

    private FilterUtils() {
    }

}
