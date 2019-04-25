package com.netflix.cloud.product.controller;

import com.netflix.cloud.product.vo.ProductInfoVO;
import com.netflix.cloud.product.vo.ProductVO;
import com.netflix.cloud.product.vo.ResultVO;
import com.netflix.cloud.product.common.DecreaseStockInput;
import com.netflix.cloud.product.common.ProductInfoOutput;
import com.netflix.cloud.product.dataobject.ProductCategory;
import com.netflix.cloud.product.dataobject.ProductInfo;
import com.netflix.cloud.product.service.CategoryService;
import com.netflix.cloud.product.service.ProductService;
import com.netflix.cloud.product.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 封装商品的请求Controller
 *
 * @author dong
 * @create 2018-09-23 下午4:58
 **/
@RequestMapping("/product")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 1.查询所有在架的商品.
     * 2.获取类目type列表.
     * 3.查询类目信息.
     * 4.构造返回数据.
     * */
    @GetMapping("/list")
    public ResultVO<ProductVO> list(HttpServletRequest request) {
        // 1.查询所有在架商品.
        List<ProductInfo> productInfoList = productService.findUpAll();

        // 2.获取所有在线商品的类别信息.
        List<Integer> productCategoryList = productInfoList.stream()
                .map(ProductInfo::getCategoryType)
                .collect(Collectors.toList());

        // 3.从数据库中查询所有类目信息.
        List<ProductCategory> categoryList = categoryService.findByCategoryTypeIn(productCategoryList);

        // 4.构造返回给前台的数据.
        List<ProductVO> productVOList = new ArrayList<>();
        for(ProductCategory category : categoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(category.getCategoryName());
            productVO.setCategoryType(category.getCategoryType());
            // 从所有在线的商品中找出所有属于当前类别的商品.
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for(ProductInfo onlineProduct : productInfoList) {
                if(onlineProduct.getCategoryType().equals(category.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    // 比较有亮点的部分使用的是BeanUtils.copyProperties,使得代码更优雅.
                    BeanUtils.copyProperties(onlineProduct, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
    }

    /**
     * 获取商品列表(主要是提供给订单服务用的).
     * @param productIdList
     * @return
     * */
    @PostMapping("/listForOrder")
    public List<ProductInfoOutput> listForOrder(@RequestBody List<String> productIdList) throws InterruptedException {
//        Thread.sleep(2000);
        // TODO 根据商品id编号查询商品详细信息.
        List<ProductInfoOutput> productInfoList = productService.findList(productIdList);
        return productInfoList;
    }

    /**
     * 对商品进行减库存.
     * @param cartList
     * */
    @PostMapping("/decreaseStock")
    public void decreaseStock(@RequestBody List<DecreaseStockInput> cartList) {
        productService.decreaseStock(cartList);
    }
}
