package com.netflix.cloud.product.utils;

import com.netflix.cloud.product.VO.ResultVO;

/**
 * ResultVO的工具类
 *
 * @author dong
 * @create 2018-09-23 下午10:40
 **/
public class ResultVOUtil {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }
}
