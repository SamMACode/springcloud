package com.netflix.could.product.VO;

import lombok.Data;

/**
 * 用于封装后台提供的数据，将其转化成为前台需要的格式
 *
 * @author dong
 * @create 2018-09-23 下午10:06
 **/
@Data
public class ResultVO<T> {

    /**
     * 表示请求后台服务返回的错误码.
     * */
    private Integer code;

    /**
     * 错误码对应的错误描述信息.
     * */
    private String msg;

    /**
     * 返回的具体的内容.
     * */
    private T data;

}
