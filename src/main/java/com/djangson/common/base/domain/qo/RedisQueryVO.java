package com.djangson.common.base.domain.qo;

import com.djangson.common.base.domain.PageInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/11/7 10:34
 */
@Getter
@Setter
@Accessors(chain=true)
public class RedisQueryVO {

    private String sortKey; // 如：system:data:port:list
    private Object hashKey; // 如：system:data:port:hash:
    private Object orderFieldKey; // 对应需要排序的Hash字段，如id，createTime
    private Integer order; // 1：正序排列，0：倒序排列
    private PageInfo pagingQuery; // 分页对象

}
