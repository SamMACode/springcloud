package com.netflix.cloud.user.filter;

import com.netflix.cloud.user.constant.RequestHeaderConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * @author Sam Ma
 * @date 2020/2/26
 * 拦截传入的Http请求,从request中获取关联id信息
 */
@Component
public class UserContextFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserContextFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // empty implement
    }

    /**
     * 继承javax.servlet.Filter接口,从请求头中获取相应信息
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // 过滤器从首部中检索关联id，并将值设置在UserContext类
        UserContextHolder.getContext()
                .setCorrelationId(httpServletRequest.getHeader(RequestHeaderConst.CORRELATION_ID));

        // 设置授权的auth-token信息、组织机构org-id信息
        UserContextHolder.getContext()
                .setAuthToken(httpServletRequest.getHeader(RequestHeaderConst.AUTH_TOKEN));
        UserContextHolder.getContext()
                .setAuthToken(httpServletRequest.getHeader(RequestHeaderConst.ORG_ID));

        // 在设置完成之后,filterChain进行放行应用执行后续的流程
        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // empty implement
    }

}
