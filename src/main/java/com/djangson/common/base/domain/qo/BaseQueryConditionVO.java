package com.djangson.common.base.domain.qo;


import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @description: 查询条件基类
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/9/23 12:32
 */
@Getter
@Setter
@Accessors(chain=true)
public abstract class BaseQueryConditionVO<T> {

    @FieldDesc("主键")
    protected Long id;

    @FieldDesc("企业主键")
    protected Long entId;

    @FieldDesc("查询关键字")
    protected String keyword;

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
