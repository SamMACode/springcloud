package com.netflix.cloud.order.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netflix.cloud.order.dataobject.OrderDetail;
import com.netflix.cloud.order.dto.OrderDTO;
import com.netflix.cloud.order.enums.OrderResultEnum;
import com.netflix.cloud.order.exception.OrderException;
import com.netflix.cloud.order.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 将orderForm的数据进行格式的转换为OrderDTO类型
 *
 * @author dong
 * @create 2018-09-26 下午10:22
 **/
@Slf4j
public class OrderForm2OrderDTO {

    public static OrderDTO convert(OrderForm orderForm) throws OrderException {
        Gson gson = new Gson();
        // 将OrderForm中的字段的数据转换成为OrderDTO的数据.
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenId());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            // 使用google的gson工具将json数据转换成为特定的类型.
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>(){}.getType());
        } catch (Exception e) {
            log.error("[json转换]错误,string={}", orderForm.getItems());
            throw new OrderException(OrderResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

}
