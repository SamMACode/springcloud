package com.netflix.cloud.product.service.impl;

import com.netflix.cloud.product.common.DecreaseStockInput;
import com.netflix.cloud.product.common.ProductInfoOutput;
import com.netflix.cloud.product.dataobject.ProductInfo;
import com.netflix.cloud.product.dto.CartDTO;
import com.netflix.cloud.product.enums.ResultEnum;
import com.netflix.cloud.product.exception.ProductException;
import com.netflix.cloud.product.repository.ProductInfoRepository;
import com.netflix.cloud.product.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void decreaseStock(List<DecreaseStockInput> cartList) {
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
        }
    }
}
