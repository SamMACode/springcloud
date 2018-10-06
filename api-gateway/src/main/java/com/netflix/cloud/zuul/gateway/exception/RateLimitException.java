package com.netflix.cloud.zuul.gateway.exception;

/**
 * 当从令牌桶中获取不到请求的时候抛出此异常
 *
 * @author dong
 * @create 2018-10-06 上午11:18
 **/
public class RateLimitException extends RuntimeException {
}
