package com.djangson.common.base.domain.vo;


import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;

import java.time.LocalDateTime;

/**
 * @Description: VO基类
 * @Author:wangqinjun@vichain.com
 * @Date: 2018/09/20 16:58
 */
public abstract class BaseVO<T> {

    @FieldDesc("主键")
    protected Long id;

    @FieldDesc("创建人主键")
    protected Long createBy;

    @FieldDesc("创建人姓名")
    protected String createName;

    @FieldDesc("创建时间")
    protected LocalDateTime createTime;

    @FieldDesc("更新人主键")
    protected Long updateBy;

    @FieldDesc("更新人姓名")
    protected String updateName;

    @FieldDesc("更新时间")
    protected LocalDateTime updateTime;

    @FieldDesc("删除标识")
    protected Integer isDeleted;

    public Long getId() {
        return id;
    }

    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public T setCreateBy(Long createBy) {
        this.createBy = createBy;
        return (T) this;
    }

    public String getCreateName() {
        return createName;
    }

    public T setCreateName(String createName) {
        this.createName = createName;
        return (T) this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public T setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return (T) this;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public T setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
        return (T) this;
    }

    public String getUpdateName() {
        return updateName;
    }

    public T setUpdateName(String updateName) {
        this.updateName = updateName;
        return (T) this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public T setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return (T) this;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public T setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
        return (T) this;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
