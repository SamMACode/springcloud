package com.netflix.could.product.controller;

import com.netflix.could.product.VO.ProductInfoVO;
import com.netflix.could.product.VO.ProductVO;
import com.netflix.could.product.VO.ResultVO;
import com.netflix.could.product.dataobject.ProductCategory;
import com.netflix.could.product.dataobject.ProductInfo;
import com.netflix.could.product.service.CategoryService;
import com.netflix.could.product.service.ProductService;
import com.netflix.could.product.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResultVO<ProductVO> list() {
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
}
