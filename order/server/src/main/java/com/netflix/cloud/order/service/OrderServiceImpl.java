package com.netflix.cloud.order.service;

import com.netflix.cloud.order.dataobject.OrderDetail;
import com.netflix.cloud.order.dataobject.OrderMaster;
import com.netflix.cloud.order.dataobject.ProductInfo;
import com.netflix.cloud.order.dto.CartDTO;
import com.netflix.cloud.order.dto.OrderDTO;
import com.netflix.cloud.order.enums.OrderStatusEnum;
import com.netflix.cloud.order.enums.PayStatusEnum;
import com.netflix.cloud.order.repository.OrderDetailRepository;
import com.netflix.cloud.order.repository.OrderMasterRepository;
import com.netflix.cloud.order.utils.KeyUtil;
import com.netflix.cloud.product.client.ProductClient;
import com.netflix.cloud.product.common.DecreaseStockInput;
import com.netflix.cloud.product.common.ProductInfoOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ProductClient productClient;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = KeyUtil.getUniqueKey();

        // 1.查询商品服务(调用product服务接口).
        List<String> productIdList = orderDTO.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        List<ProductInfoOutput> productInfos = productClient.listForOrder(productIdList);

        // 2.计算Order订单中的商品总价.
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        for(OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            for(ProductInfoOutput productInfo : productInfos) {
                if(orderDetail.getProductId().equals(productInfo.getProductId())) {
                    // 计算公式为 总价 = 单价 * 数量.
                    orderAmount = productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmount);
                    // 将查询到的productInfo信息拷贝到orderDetail中.
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.getUniqueKey());
                    // 订单详情入库.
                    orderDetailRepository.save(orderDetail);
                }
            }
        }

        // 3.扣库存(调用product服务接口).
        List<DecreaseStockInput> decreaseStockList = orderDTO.getOrderDetailList().stream()
                .map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockList);

        // 4.订单入库(将生成的订单主表信息Master以及明细表detail信息保存到数据库中).
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getOrderStatus());
        orderMaster.setPayStatus(PayStatusEnum.WAITING.getCode());

        orderMasterRepository.save(orderMaster);
        return orderDTO;
    }
}
