package com.djangson.common.base.domain.vo;

import org.springframework.data.redis.core.DefaultTypedTuple;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/11/8 11:45
 */
public class CacheBaseVO<T> extends DefaultTypedTuple<T> {
    public CacheBaseVO(T value, Double score) {
        super(value, score);
    }
}
