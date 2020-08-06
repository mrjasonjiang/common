package com.djangson.common.base.domain;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.constant.Constants;
import com.djangson.common.constant.ErrorConstants;
import com.djangson.common.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * 用于向前端返回统一的结果对象
 * @param <T>
 */
@Getter
@Setter
@Accessors(chain=true)
public final class Result<T> {

    @FieldDesc("操作标识，0：失败，1：成功")
    private int success = 0; // 操作标识，默认为失败

    @FieldDesc("错误提示码，默认00000")
    private String resultCode; // 结果编码

    @FieldDesc("异常提示信息")
    private String message; // 提示信息

    @FieldDesc("响应对象")
    private T model; // 结果对象

    @FieldDesc("分页信息")
    private PageInfo pageInfo; // 分页信息对象

    @FieldDesc("扩展字段")
    private Object extra; // 扩展字段

    // 禁止空参构造
    private Result() {
    }

    public static <T> Result<T> createWithErrorCode(String errorCode) {
        return createWithErrorMessage(null, errorCode);
    }

    public static <T> Result<T> createWithErrorMessage(String message, String errorCode) {
        message = StringUtils.isBlank(message) ? "操作失败！" : message;
        errorCode = StringUtils.isBlank(errorCode) ? ErrorConstants.OPERATION_FAIL : errorCode;
        return new Result<T>().setMessage(message).setResultCode(errorCode);
    }

    public static <T> Result<T> createWithSuccessMessage() {
        return createWithSuccessMessage(null);
    }

    public static <T> Result<T> createWithSuccessMessage(String message) {
        message = StringUtils.isBlank(message) ? "操作成功！" : message;
        Result<T> result = new Result<T>().setSuccess(Constants.YES).setResultCode(ErrorConstants.OPERATION_SUCCESS).setMessage(message);
        return result;
    }

    public static <T> Result<T> createWithModel(String message, T model) {
        return (Result<T>) createWithSuccessMessage(message).setModel(model);
    }

    public static <T> Result<T> createWithModel(String message, T model, Object extra) {
        return (Result<T>) createWithSuccessMessage(message).setModel(model).setExtra(extra);
    }

    public static <T> Result<T> createWithModels(String message, T models) {
        return ((Result<T>) createWithSuccessMessage(message)).setModel(models);
    }

    public static <T> Result<T> createWithModels(String message, T models, Object extra) {
        return ((Result<T>) createWithSuccessMessage(message)).setModel(models).setExtra(extra);
    }

    public static <T> Result<T> createWithPaging(String message, T models, PageInfo pagingInfo) {
        return ((Result<T>) createWithSuccessMessage(message)).setModel(models).setPageInfo(pagingInfo);
    }

    public static <T> Result<T> createWithPaging(String message, T models, PageInfo pagingInfo, Object extra) {
        return ((Result<T>) createWithSuccessMessage(message)).setModel(models).setPageInfo(pagingInfo).setExtra(extra);
    }

}
