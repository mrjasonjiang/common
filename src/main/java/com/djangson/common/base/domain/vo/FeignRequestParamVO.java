package com.djangson.common.base.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2020/5/20 16:15
 */
@Getter
@Setter
@Accessors(chain=true)
public class FeignRequestParamVO {

    private Object requestParam;
    private UserSessionVO userSessionVO;

}
