package com.netflix.cloud.order.repository;

import com.netflix.cloud.order.OrderApplicationTests;
import com.netflix.cloud.order.dataobject.OrderMaster;
import com.netflix.cloud.order.enums.OrderStatusEnum;
import com.netflix.cloud.order.enums.PayStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderMasterRepositoryTest extends OrderApplicationTests {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Test
    public void testSaveMasterOrder() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("12345678");
        orderMaster.setBuyerName("廖师兄");
        orderMaster.setBuyerPhone("18829012054");
        orderMaster.setBuyerAddress("餐厅的总部地址");
        orderMaster.setBuyerOpenid("123455wechat");
        orderMaster.setOrderAmount(new BigDecimal(2.5));
        // 设置订单的状态,表示订单已经完成状态.
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getOrderStatus());
        orderMaster.setPayStatus(PayStatusEnum.FINISHED.getCode());
        // 将测试数据写入到mysql数据库中.
        OrderMaster result = orderMasterRepository.save(orderMaster);
        Assert.assertTrue(result != null);
    }

}