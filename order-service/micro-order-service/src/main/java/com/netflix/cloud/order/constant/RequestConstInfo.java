package com.netflix.cloud.order.constant;

/**
 * @author Sam Ma
 * @date 2020/08/08
 * 定义rest request请求的url地址
 */
public class RequestConstInfo {

    /**
     * 获取产品基本信息
     */
    public static final String GET_PRODUCT_MSG = "getProductMsg";

    /**
     * 获取产品列表
     */
    public static final String GET_PRODUCT_LIST = "getProductList";

    /**
     * 减少产品的库存量
     */
    public static final String DECREASE_PRODUCT_STOCK = "productDecreaseStock";

    /**
     * @RefreshScope动态刷新url
     */
    public static final String DYNAMIC_REFRESH_SCOPE = "/gril/refreshconfig";

    /**
     * 创建订单order的接口
     */
    public static final String CREATE_ORDER = "/order/create";

    /**
     * 结束订单接口
     */
    public static final String FINISH_ORDER = "/order/finish";

    /**
     * 异步stream减少订单
     */
    public static final String ASYNC_DECREASE_ORDER = "decreaseStockAsync";

    /**
     * hystrix查询产品信息
     */
    public static final String HYSTRIX_GET_PRODUCT_LIST = "getProductInfoList";

    private RequestConstInfo() {

    }
}
