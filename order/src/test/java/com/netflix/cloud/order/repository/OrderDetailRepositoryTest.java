package com.netflix.cloud.order.repository;

import com.netflix.cloud.order.OrderApplicationTests;
import com.netflix.cloud.order.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@Component
public class OrderDetailRepositoryTest extends OrderApplicationTests {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void testSaveOrderDetailInfo() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("20180925001003");
        orderDetail.setOrderId("12345678");
        orderDetail.setProductIcon("http://www.icon.com/");
        orderDetail.setProductId("335");
        orderDetail.setProductName("皮蛋粥");
        orderDetail.setProductPrice(new BigDecimal(0.2));
        orderDetail.setProductQuantity(2);

        OrderDetail result = orderDetailRepository.save(orderDetail);
        Assert.assertTrue(result != null);
    }
}