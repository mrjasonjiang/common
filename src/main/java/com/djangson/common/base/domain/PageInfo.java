package com.djangson.common.base.domain;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.constant.Constants;
import com.djangson.common.constant.ErrorConstants;
import com.djangson.common.util.ExceptionUtil;
import com.djangson.common.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public final class PageInfo {

    @FieldDesc("查询页码，默认第1页")
    private long pageIndex = Constants.DEFAULT_PAGE_INDEX;

    @FieldDesc("每页大小，默认10，最大500")
    private long pageSize = Constants.DEFAULT_PAGE_SIZE;

    @FieldDesc("数据总量")
    private Long total;

    public PageInfo setPageSize(long pageSize) {
        if (pageSize > Constants.DEFAULT_MAX_PAGE_SIZE) {
            ExceptionUtil.rollback("分页参数有误！", ErrorConstants.OPERATION_FAIL);
        }
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
