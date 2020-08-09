package com.netflix.cloud.user.interceptor;

import com.netflix.cloud.user.constant.RequestHeaderConst;
import com.netflix.cloud.user.filter.UserContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author Sam Ma
 * @date 2020/2/26
 * 自定义Spring拦截器，将关联id注入基于http的传出服务请求总
 */
public class UserContextInterceptor implements ClientHttpRequestInterceptor {

    /**
     * intercept方法在RestTemplate发生实际的http服务调用前被调用
     * @param request
     * @param body
     * @param execution
     * @return
     * @throws IOException
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        // 在HttpServletRequest中设置"tmx-correlation-id"信息, 以及用于授权的auth-token
        headers.add(RequestHeaderConst.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        headers.add(RequestHeaderConst.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());

        return execution.execute(request, body);
    }

}
