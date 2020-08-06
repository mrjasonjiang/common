package com.djangson.common.base.domain.po;

import com.djangson.common.constant.Constants;
import com.djangson.common.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/2/22 10:34
 */
@Getter
@Setter
@Accessors(chain=true)
public abstract class Model<T extends Model<?>> implements Serializable {

    protected Long createBy;
    protected LocalDateTime createTime;
    protected Long updateBy;
    protected LocalDateTime updateTime;
    protected Integer isDeleted;

    public T initForUpdate(Long userId, LocalDateTime operationTime) {
        this.setUpdateBy(userId);
        this.setUpdateTime(operationTime);
        this.setIsDeleted(Constants.NO);
        return (T) this;
    }

    public T initForInsert(Long userId, LocalDateTime operationTime) {
        this.setCreateBy(userId);
        this.setCreateTime(operationTime);
        this.initForUpdate(userId, operationTime);
        return (T) this;
    }

    public T initForDelete(Long userId, LocalDateTime operationTime) {
        this.initForUpdate(userId, operationTime);
        this.setIsDeleted(Constants.YES);
        return (T) this;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
