package com.netflix.cloud.order.repository;

import com.netflix.cloud.order.dataobject.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 订单主表的Repository生成类
 *
 * @author dong
 * @create 2018-09-25 下午10:15
 **/
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String>{
}
