package com.netflix.cloud.order.service;

import com.netflix.cloud.order.dataobject.OrderMaster;
import com.netflix.cloud.order.dto.OrderDTO;
import com.netflix.cloud.order.enums.OrderStatusEnum;
import com.netflix.cloud.order.enums.PayStatusEnum;
import com.netflix.cloud.order.repository.OrderDetailRepository;
import com.netflix.cloud.order.repository.OrderMasterRepository;
import com.netflix.cloud.order.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Order订单服务实现
 *
 * @author dong
 * @create 2018-09-25 下午11:13
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    public OrderDTO create(OrderDTO orderTransObject) {
        /**
         * todo 1.查询商品信息(调用product module服务).
         * todo 2.计算order中的商品总价.
         * todo 3.扣库存(调用product module服务).
         * 4.订单入库(将生成的订单主表信息Master以及明细表detail信息保存到数据库中).
         * */
        OrderMaster orderMaster = new OrderMaster();
        orderTransObject.setOrderId(KeyUtil.getUniqueKey());
        BeanUtils.copyProperties(orderTransObject, orderMaster);
        orderMaster.setOrderAmount(new BigDecimal(5));
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getOrderStatus());
        orderMaster.setPayStatus(PayStatusEnum.WAITING.getCode());

        orderMasterRepository.save(orderMaster);
        return orderTransObject;
    }
}
