package com.netflix.cloud.order.vo;

import lombok.Data;

/**
 * 用于封装最后的处理结果
 *
 * @author dong
 * @create 2018-09-26 下午10:51
 **/
@Data
public class ResultVO<T> {

    private Integer code;

    private String message;

    private T data;
}
