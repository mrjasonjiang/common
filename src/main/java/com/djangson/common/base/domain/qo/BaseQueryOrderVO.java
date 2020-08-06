package com.djangson.common.base.domain.qo;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: 排序通用对象
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/3/10 13:12
 */
@Getter
@Setter
@Accessors(chain=true)
public final class BaseQueryOrderVO {

    @FieldDesc("排序字段")
    protected String field;

    @FieldDesc("1：正序，0：倒序")
    protected Integer order;

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
