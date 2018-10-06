package com.netflix.cloud.order.controller;

import com.netflix.cloud.order.VO.ResultVO;
import com.netflix.cloud.order.VO.ResultVOUtils;
import com.netflix.cloud.order.converter.OrderForm2OrderDTO;
import com.netflix.cloud.order.dto.OrderDTO;
import com.netflix.cloud.order.enums.OrderResultEnum;
import com.netflix.cloud.order.exception.OrderException;
import com.netflix.cloud.order.form.OrderForm;
import com.netflix.cloud.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单的Controller实体类
 *
 * @author dong
 * @create 2018-09-25 下午10:56
 **/
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 1.第一步对ajax传递来的参数进行校验.
     * 2.查询商品服务(调用product服务接口).
     * 3.计算Order订单中的商品总价.
     * 4.扣库存(调用product服务接口).
     * 5.在order部分生成订单流水信息(master表和detail表).
     * */
    @RequestMapping("/create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) throws OrderException {
        // 1.第一步对ajax传递来的参数进行校验.
        if(bindingResult.hasErrors()) {
            log.error("[创建订单]参数不正确, orderForm={}", orderForm);
            throw new OrderException(OrderResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        // 需要将数据进行转换 orderForm -> orderDTO.
        OrderDTO orderDTO = OrderForm2OrderDTO.convert(orderForm);
        // 对创建订单里的明细数据进行校验
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            throw new OrderException(OrderResultEnum.CART_EMPTY);
        }

        // 5.调用订单Order服务创建订单信息.
        OrderDTO result = orderService.create(orderDTO);
        // 返回处理后的结果,包括订单id编号以及订单描述信息code&message.
        Map<String, String> map = new HashMap<>();
        map.put("orderId", result.getOrderId());
        return ResultVOUtils.success(map);
    }

    @PostMapping("/finish")
    public ResultVO<OrderDTO> finish(@RequestParam("orderId")String orderId) throws OrderException {
        return ResultVOUtils.success(orderService.finish(orderId));
    }

}