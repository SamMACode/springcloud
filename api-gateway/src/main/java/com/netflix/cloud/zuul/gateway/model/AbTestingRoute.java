package com.netflix.cloud.zuul.gateway.model;

/**
 * @author Sam Ma
 * @date 2020/2/26
 * 封装A/B测试的实体类信息，封装服务名称、比重、是否处于活跃状态
 */
public class AbTestingRoute {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 路由是否处于活跃的状态("N"为不活跃)
     */
    private String active;

    /**
     * 终点值
     */
    private String endPoint;

    /**
     * A/B请求分发的比重
     */
    private Integer weight;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

}
