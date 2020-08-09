package com.netflix.cloud.product.constant;

/**
 * @author Sam Ma
 * @date 2020/08/08
 * 定义product service服务rest接口api
 */
public class RequestInfoConst {

    /**
     * 获取所有产品列表的api接口
     */
    public static final String GET_PRODUCT_LIST = "/product/list";

    /**
     * 根据订单号查询该订单包含的商品列表
     */
    public static final String GET_PRODUCT_LIST_FROM_ORDER = "/product/listForOrder";

    /**
     * 根据产品编号id减少产品的库存
     */
    public static final String DECREASE_STOCK_BY_PRODUCT_ID = "/product/decreaseStock";

    private RequestInfoConst() {
    }

}
