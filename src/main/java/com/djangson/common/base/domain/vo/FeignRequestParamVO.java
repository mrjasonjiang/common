package com.djangson.common.base.domain.vo;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2020/5/20 16:15
 */
public class FeignRequestParamVO {

    private Object requestParam;
    private UserSessionVO userSessionVO;

    public FeignRequestParamVO(Object requestParam, UserSessionVO userSessionVO) {
        this.requestParam = requestParam;
        this.userSessionVO = userSessionVO;
    }

    public Object getRequestParam() {
        return requestParam;
    }

    public FeignRequestParamVO setRequestParam(Object requestParam) {
        this.requestParam = requestParam;
        return this;
    }

    public UserSessionVO getUserSessionVO() {
        return userSessionVO;
    }

    public FeignRequestParamVO setUserSessionVO(UserSessionVO userSessionVO) {
        this.userSessionVO = userSessionVO;
        return this;
    }
}
