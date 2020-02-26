package com.netflix.cloud.zuul.gateway.filter;

import com.netflix.cloud.zuul.gateway.model.AbTestingRoute;
import com.netflix.cloud.zuul.gateway.utils.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;


/**
 * @author Sam Ma
 * @date 2020/2/26
 * 构建动态路由过滤器，从而允许对新版本的服务进行A/B测试
 */
@Component
public class SpecialRoutesFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialRoutesFilter.class);

    /**
     * Filter路由的优先级顺序
     */
    private static final int FILTER_ORDER = 1;

    /**
     * helper变量是类ProxyRequestHelper类型的一个变量变量,带有用于代理服务请求的辅助方法
     */
    private ProxyRequestHelper helper = new ProxyRequestHelper();

    /**
     * 过滤器的类型为路由"route"
     *
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 真正的业务逻辑调用,以确定该服务id是否有路由记录
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 执行对SpecialRoutes服务的调用，以确定该服务id是否有路由记录
        AbTestingRoute abTestingRoute = getAbRoutingInfo(FilterUtils.getServiceId());

        if (null != abTestingRoute && useSpecialRoute(abTestingRoute)) {
            // 若存在路由记录, 则将完整的url(包含路径)构建由specialroutes服务指定的服务位置
            String routeUrl = buildRouteString(ctx.getRequest().getRequestURI(),
                    abTestingRoute.getEndPoint(), ctx.get("serviceId").toString());
            // forwardToSpecialRoute()方法完成转发到其他服务的工作
            forwardToSpecialRoute(routeUrl);
        }
        return null;
    }

    /**
     * 根据服务名称以查看路由记录是否存在
     * @param serviceName
     * @return
     */
    private AbTestingRoute getAbRoutingInfo(String serviceName) {
        ResponseEntity<AbTestingRoute> restExchange = null;

        try {
            RestTemplate restTemplate = new RestTemplate();
            // 若路由服务没有找到记录(它将返回Http状态码404),该方法将返回空值
            restExchange = restTemplate.exchange("http://specialroutesservice/v1/route/abtesting/{serviceName}",
                    HttpMethod.GET, null, AbTestingRoute.class, serviceName);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                LOGGER.error("restTemplate call service cause exception: [{}]", ex);
                return null;
            }
        }
        return restExchange.getBody();
    }

    /**
     * 决定是否使用替代服务路由
     * @param testRoute
     * @return
     */
    public boolean useSpecialRoute(AbTestingRoute testRoute) {
        Random random = new Random();
        if (testRoute.getActive().equals("N")) {
            return false;
        }
        // 确定是否应该使用替代服务路由
        int value = random.nextInt((10 - 1) + 1) + 1;
        if (testRoute.getWeight() < value) {
            return true;
        }
        return false;
    }

    /**
     * 使用endPoint构建路由的String
     * @param oldEndpoint
     * @param newEndpoint
     * @param serviceName
     * @return
     */
    private String buildRouteString(String oldEndpoint, String newEndpoint, String serviceName) {
        int index = oldEndpoint.indexOf(serviceName);

        String strippedRoute = oldEndpoint.substring(index + serviceName.length());
        System.out.println("Target route: " + String.format("%s/%s", newEndpoint, strippedRoute));
        return String.format("%s/%s", newEndpoint, strippedRoute);
    }

    /**
     * 转发路由方法: SpecialRoutesFilter中出现的大部分工作是到下游服务的路由的实际转发
     * @param route
     */
    private void forwardToSpecialRoute(String route) {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        // 创建将发送到服务的所有Http请求首部的副本, 以及所有http请求参数的副本
        MultiValueMap<String, String> headers = helper.buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = helper.buildZuulRequestQueryParams(request);
        String verb = getVerb(request);
        InputStream requestEntity = getRequestBody(request);
        if (request.getContentLength() < 0) {
            context.setChunkedRequestBody();
        }

        this.helper.addIgnoredHeaders();
        CloseableHttpClient httpClient = null;
        HttpResponse response = null;

        try {
            httpClient = HttpClients.createDefault();
            response = forward(httpClient, verb, route, request, headers,
                    params, requestEntity);
            setResponse(response);
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            try {
                httpClient.close();
            } catch (IOException ex) {
            }
        }
    }


    private String getVerb(HttpServletRequest request) {
        String sMethod = request.getMethod();
        return sMethod.toUpperCase();
    }

    private HttpHost getHttpHost(URL host) {
        HttpHost httpHost = new HttpHost(host.getHost(), host.getPort(),
                host.getProtocol());
        return httpHost;
    }

    private Header[] convertHeaders(MultiValueMap<String, String> headers) {
        List<Header> list = new ArrayList<>();
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                list.add(new BasicHeader(name, value));
            }
        }
        return list.toArray(new BasicHeader[0]);
    }

    private InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = request.getInputStream();
        } catch (IOException ex) {
            // no requestBody is ok.
        }
        return requestEntity;
    }

    private HttpResponse forward(HttpClient httpclient, String verb, String uri,
                                 HttpServletRequest request, MultiValueMap<String, String> headers,
                                 MultiValueMap<String, String> params, InputStream requestEntity)
            throws Exception {
        Map<String, Object> info = this.helper.debug(verb, uri, headers, params,
                requestEntity);
        URL host = new URL(uri);
        HttpHost httpHost = getHttpHost(host);

        HttpRequest httpRequest;
        int contentLength = request.getContentLength();
        InputStreamEntity entity = new InputStreamEntity(requestEntity, contentLength,
                request.getContentType() != null
                        ? ContentType.create(request.getContentType()) : null);
        switch (verb.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(uri);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uri);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(uri);
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(verb, uri);

        }
        try {
            httpRequest.setHeaders(convertHeaders(headers));
            HttpResponse zuulResponse = forwardRequest(httpclient, httpHost, httpRequest);

            return zuulResponse;
        } finally {
        }
    }

    private HttpResponse forwardRequest(HttpClient httpclient, HttpHost httpHost,
                                        HttpRequest httpRequest) throws IOException {
        return httpclient.execute(httpHost, httpRequest);
    }

    private void setResponse(HttpResponse response) throws IOException {
        this.helper.setResponse(response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                revertHeaders(response.getAllHeaders()));
    }

    private MultiValueMap<String, String> revertHeaders(Header[] headers) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        for (Header header : headers) {
            String name = header.getName();
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<String>());
            }
            map.get(name).add(header.getValue());
        }
        return map;
    }

}
