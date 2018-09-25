package com.netflix.cloud.order.repository;

import com.netflix.cloud.order.dataobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 订单明细表的Repository实现
 *
 * @author dong
 * @create 2018-09-25 下午10:16
 **/
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
