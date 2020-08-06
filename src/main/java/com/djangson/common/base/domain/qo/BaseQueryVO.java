package com.djangson.common.base.domain.qo;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.base.domain.PageInfo;
import com.djangson.common.constant.Constants;
import com.djangson.common.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @description: 查询对象基类
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/9/23 12:34
 * @modified:
 */
@Getter
@Setter
@Accessors(chain=true)
public abstract class BaseQueryVO<T> {

    @FieldDesc("分页条件")
    protected PageInfo pagingQuery;

    @FieldDesc("排序条件")
    protected List<BaseQueryOrderVO> queryOrderList;

    /**
     * 获取排序字符串
     * @param orderMap
     * @return
     */
    public String getOrderStr(Map<String, String> orderMap) {
        return getOrderStr(this.queryOrderList, orderMap);
    }

    /**
     * 获取排序字符串
     * @param orderMap
     * @return
     */
    public static String getOrderStr(List<BaseQueryOrderVO> queryOrderList, Map<String, String> orderMap) {

        // 若无排序字段，则直接返回
        if (CollectionUtils.isEmpty(queryOrderList) || CollectionUtils.isEmpty(orderMap)) {
            return null;
        }

        // 拼接排序语句
        StringBuilder builder = new StringBuilder();
        queryOrderList.stream().filter(baseQueryOrderVO -> baseQueryOrderVO != null).forEach(baseQueryOrderVO -> {
            String field = orderMap.get(baseQueryOrderVO.getField());
            if (baseQueryOrderVO.getOrder() != null && StringUtils.isNotBlank(field)) {
                if (baseQueryOrderVO.getOrder() == 0) {
                    builder.append(field).append(" desc, ");
                } else {
                    builder.append(field).append(" asc, ");
                }
            }
        });

        // 除去最后的逗号
        return builder.length() > 0 ? builder.delete(builder.lastIndexOf(","), builder.length()).toString() : Constants.BLANK_STRING;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
