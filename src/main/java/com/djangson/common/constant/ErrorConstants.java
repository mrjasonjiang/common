package com.djangson.common.constant;

/**
 * @Description: 系统公共错误提示码
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/2/14 10:04
 */
public class ErrorConstants {

    public static final String PHONE_EXISTED = "02001"; // 手机号码已存在
    public static final String PHONE_NOT_EXIST = "02002"; // 手机号码不存在
    public static final String VALIDATE_CODE_INVALID = "02003"; // 验证码已失效
    public static final String INVITE_CODE_INVALID = "02004"; // 邀请码无效
    public static final String USER_ALREADY_BIND_WECHAT = "02005"; // 账户已绑定微信
    public static final String STRING_IS_ILLEGAL = "02006"; // 非法字符

    public static final String OPERATION_SUCCESS = "00000";
    public static final String OPERATION_FAIL = "00001";
    public static final String LOGIN_TIMEOUT = "00098";
}
