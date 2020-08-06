package com.djangson.common.base.domain.vo;


import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: VO基类
 * @Author:wangqinjun@vichain.com
 * @Date: 2018/09/20 16:58
 */
@Getter
@Setter
@Accessors(chain=true)
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

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
