package com.cloud.nextflix.eureka.client.filter;

import com.cloud.nextflix.eureka.client.entity.UserContext;

/**
 * @author Sam Ma
 * @date 2020/2/26
 * 用于将UserContext存储在ThreadLocal变量中，该变量可以在处理用户请求的线程中调用的任何方法中访问
 */
public class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    /**
     * 获取当前HttpServletRequest请求的上线文
     * @return
     */
    public static final UserContext getContext() {
        UserContext context = userContext.get();
        if (null == context) {
            context = new UserContext();
            userContext.set(context);
        }
        return userContext.get();
    }

    /**
     * 设置当前请求的Context的上下文关系
     * @param context
     */
    public static final void setContext(UserContext context) {
        userContext.set(context);
    }

}
