package com.djangson.common.base.domain.qo;

import com.djangson.common.base.domain.PageInfo;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/11/7 10:34
 */
public class RedisQueryVO {

    private String sortKey; // 如：system:data:port:list
    private Object hashKey; // 如：system:data:port:hash:
    private Object orderFieldKey; // 对应需要排序的Hash字段，如id，createTime
    private Integer order; // 1：正序排列，0：倒序排列
    private PageInfo pagingQuery; // 分页对象

    public String getSortKey() {
        return sortKey;
    }

    public RedisQueryVO setSortKey(String sortKey) {
        this.sortKey = sortKey;
        return this;
    }

    public Object getHashKey() {
        return hashKey;
    }

    public RedisQueryVO setHashKey(Object hashKey) {
        this.hashKey = hashKey;
        return this;
    }

    public Object getOrderFieldKey() {
        return orderFieldKey;
    }

    public RedisQueryVO setOrderFieldKey(Object orderFieldKey) {
        this.orderFieldKey = orderFieldKey;
        return this;
    }

    public Integer getOrder() {
        return order;
    }

    public RedisQueryVO setOrder(Integer order) {
        this.order = order;
        return this;
    }

    public PageInfo getPagingQuery() {
        return pagingQuery;
    }

    public RedisQueryVO setPagingQuery(PageInfo pagingQuery) {
        this.pagingQuery = pagingQuery;
        return this;
    }
}
