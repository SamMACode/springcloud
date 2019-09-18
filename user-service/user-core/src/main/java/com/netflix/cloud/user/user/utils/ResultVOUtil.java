package com.netflix.cloud.user.user.utils;


import com.netflix.cloud.user.user.vo.ResultVO;
import com.netflix.cloud.user.user.enums.ResultEnum;

/**
 * ResultVO的工具类
 *
 * @author dong
 * @create 2018-09-23 下午10:40
 **/
public class ResultVOUtil {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setData(object);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success() {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO error(ResultEnum resultEnum) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(resultEnum.getCode());
        resultVO.setMsg(resultEnum.getMessage());
        return resultVO;
    }
}
