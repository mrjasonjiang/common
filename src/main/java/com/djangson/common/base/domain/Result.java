package com.djangson.common.base.domain;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.constant.Constants;
import com.djangson.common.constant.ErrorConstants;
import com.djangson.common.util.JsonUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于向前端返回统一的结果对象
 * @param <T>
 */
public final class Result<T> {

    @FieldDesc("操作标识，0：失败，1：成功")
    private int success = 0; // 操作标识，默认为失败

    @FieldDesc("错误提示码，默认00000")
    private String resultCode; // 结果编码

    @FieldDesc("异常提示信息")
    private String message; // 提示信息

    @FieldDesc("响应对象(单条)")
    private T model; // 结果对象

    @FieldDesc("响应对象(多条)")
    private List<T> models; // 结果集对象

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

    public static <T> Result<T> createWithModels(String message, List<T> models) {
        models = models == null ? new ArrayList<T>(0) : models;
        return ((Result<T>) createWithSuccessMessage(message)).setModels(models);
    }

    public static <T> Result<T> createWithModels(String message, List<T> models, Object extra) {
        models = models == null ? new ArrayList<T>(0) : models;
        return ((Result<T>) createWithSuccessMessage(message)).setModels(models).setExtra(extra);
    }

    public static <T> Result<T> createWithPaging(String message, List<T> models, PageInfo pagingInfo) {
        models = models == null ? new ArrayList<T>(0) : models;
        return ((Result<T>) createWithSuccessMessage(message)).setModels(models).setPageInfo(pagingInfo);
    }

    public static <T> Result<T> createWithPaging(String message, List<T> models, PageInfo pagingInfo, Object extra) {
        models = models == null ? new ArrayList<T>(0) : models;
        return ((Result<T>) createWithSuccessMessage(message)).setModels(models).setPageInfo(pagingInfo).setExtra(extra);
    }

    public int getSuccess() {
        return success;
    }

    public Result<T> setSuccess(int success) {
        this.success = success;
        return this;
    }

    public String getResultCode() {
        return resultCode;
    }

    public Result<T> setResultCode(String resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getModel() {
        return model;
    }

    public Result<T> setModel(T model) {
        this.model = model;
        return this;
    }

    public List<T> getModels() {
        return models;
    }

    public Result<T> setModels(List<T> models) {
        this.models = models;
        return this;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public Result<T> setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public Object getExtra() {
        return extra;
    }

    public Result<T> setExtra(Object extra) {
        this.extra = extra;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
