package com.djangson.common.base.domain.qo;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;

/**
 * @Description: 排序通用对象
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/3/10 13:12
 */
public final class BaseQueryOrderVO {

    @FieldDesc("排序字段")
    protected String field;

    @FieldDesc("1：正序，0：倒序")
    protected Integer order;

    public String getField() {
        return field;
    }

    public BaseQueryOrderVO setField(String field) {
        this.field = field;
        return this;
    }

    public Integer getOrder() {
        return order;
    }

    public BaseQueryOrderVO setOrder(Integer order) {
        this.order = order;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
