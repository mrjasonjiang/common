package com.djangson.common.base.domain.qo;


import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;

/**
 * @description: 查询条件基类
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/9/23 12:32
 */
public abstract class BaseQueryConditionVO<T> {

    @FieldDesc("主键")
    protected Long id;

    @FieldDesc("企业主键")
    protected Long entId;

    @FieldDesc("查询关键字")
    protected String keyword;

    public Long getId() {
        return id;
    }

    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }

    public Long getEntId() {
        return entId;
    }

    public T setEntId(Long entId) {
        this.entId = entId;
        return (T) this;
    }

    public String getKeyword() {
        return keyword;
    }

    public T setKeyword(String keyword) {
        this.keyword = keyword;
        return (T) this;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
