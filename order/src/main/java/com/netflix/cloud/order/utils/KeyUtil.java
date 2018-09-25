package com.netflix.cloud.order.utils;

import java.util.Random;

/**
 * 生成Order订单的key值
 *
 * @author dong
 * @create 2018-09-25 下午11:27
 **/
public class KeyUtil {

    /**
     * 生成唯一的主键 格式:主键 + 时间戳.
     * */
    public static synchronized String getUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(9000000) + 1000000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
