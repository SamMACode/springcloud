package com.netflix.cloud.order.VO;

/**
 * 用于封装最后处理的结果信息
 *
 * @author dong
 * @create 2018-09-26 下午10:53
 **/
public class ResultVOUtils {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        resultVO.setData(object);
        return resultVO;
    }
}
