package com.djangson.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djangson.common.base.domain.PageInfo;
import com.djangson.common.constant.Constants;

public class PageUtil {

    /**
     * 检查分页条件并返回分页对象，若对象不存在，则初始化为默认值
     * @param pageInfo
     * @return
     */
    public static Page checkAndInitPage(PageInfo pageInfo) {
        return pageInfo == null ? new Page() : new Page(pageInfo.getPageIndex(), pageInfo.getPageSize());
    }

    /**
     * 根据分页对象，生成分页结果
     * @param page
     * @param pageInfo
     */
    public static void initPageInfo(Page page, PageInfo pageInfo) {
        if (page != null && pageInfo != null) {
            pageInfo.setPageIndex(page.getCurrent()).setPageSize(page.getSize()).setTotal(page.getTotal());
        }
    }

    /**
     * 根据分页对象，生成分页结果
     * @param count
     */
    public static Page initPageLimitCount(long count) {
        return new Page(1, count < 1L ? 1L : count);
    }

    /**
     * 根据分页对象，生成分页结果
     */
    public static PageInfo initMaxPageLimit() {
        return new PageInfo().setPageSize(Constants.DEFAULT_MAX_PAGE_SIZE);
    }
}
