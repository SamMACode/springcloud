package com.netflix.cloud.product.service.impl;

import com.netflix.cloud.product.common.DecreaseStockInput;
import com.netflix.cloud.product.common.ProductInfoOutput;
import com.netflix.cloud.product.dataobject.ProductInfo;
import com.netflix.cloud.product.enums.ResultEnum;
import com.netflix.cloud.product.exception.ProductException;
import com.netflix.cloud.product.repository.ProductInfoRepository;
import com.netflix.cloud.product.service.ProductService;
import com.netflix.cloud.product.utils.JsonUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品服务的业务实现类
 *
 * @author dong
 * @create 2018-09-23 下午9:43
 **/
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public List<ProductInfo> findUpAll() {
        return this.productInfoRepository.findProductInfoByProductStatus(0);
    }

    @Override
    public List<ProductInfoOutput> findList(List<String> productList) {
        return productInfoRepository.findByProductIdIn(productList).stream()
                .map(ele -> {
                    ProductInfoOutput outputInfo = new ProductInfoOutput();
                    BeanUtils.copyProperties(ele, outputInfo);
                    return outputInfo;
                })
                .collect(Collectors.toList());

    }

    @Override
    public void decreaseStock(List<DecreaseStockInput> cartList) {
        // 对购物车中的商品进行扣库存的操作.
        List<ProductInfo> productInfoList = decreaseStockProcess(cartList);

        List<ProductInfoOutput> productInfoOutputList = productInfoList.stream().map(ele -> {
            ProductInfoOutput output = new ProductInfoOutput();
            BeanUtils.copyProperties(ele, output);
            return output;
        }).collect(Collectors.toList());
        // 扣库存完成后发送mq消息(发送的是整个购物车中的扣库存信息list).
        amqpTemplate.convertAndSend("productInfo", JsonUtil.toJson(productInfoOutputList));
    }

    /**
     * 对于购物车中的所有商品先执行扣库存完成后，再向消息队列发消息;
     *  (为了避免当购物车中的商品出现了库存不足时导致order服务模块与product数据库库存数量不一致).
     * @param cartList
     * @return
     * */
    @Transactional
    public List<ProductInfo> decreaseStockProcess(List<DecreaseStockInput> cartList) {
        List<ProductInfo> productList = new ArrayList<>();
        for(DecreaseStockInput stockInput : cartList) {
            // 判断商品是否存在.
            Optional<ProductInfo> optionalProductInfo = productInfoRepository.findById(stockInput.getProductId());
            if(!optionalProductInfo.isPresent()) {
                throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            // 判断库存是否充足(库存 - 购物车中的数量 > 0)
            ProductInfo productInfo = optionalProductInfo.get();
            Integer result = productInfo.getProductStock() - stockInput.getProductQuantity();
            if(result < 0) {
                throw new ProductException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setProductStock(result);
            productInfoRepository.save(productInfo);
            productList.add(productInfo);
        }
        return productList;
    }

}
