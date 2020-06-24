package com.djangson.common.util;

import com.djangson.common.base.domain.vo.UserSessionVO;
import com.djangson.common.constant.CacheKeyConstants;
import com.djangson.common.constant.Constants;
import com.djangson.common.constant.ErrorConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Session工具类
 * @Author: wangqinjun@vichain.com
 * @date 2020/04/26
 */
public class SessionUtil {

    /**
     * 判断请求是否是Feign 请求
     * @return
     */
    public static boolean isFeignRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String feignToken = request == null ? null : request.getHeader(Constants.FEIGN_TOKEN_HEADER);
        return StringUtils.isNotBlank(feignToken) && feignToken.equals(Constants.FEIGN_TOKEN_VALUE);
    }

    /**
     * 获取用户Session信息
     * @return
     */
    public static UserSessionVO getUserSessionInfo() {
        return SessionUtil.getUserSessionInfo(true);
    }

    /**
     * 获取用户Session信息
     * @param withError
     * @return
     */
    public static UserSessionVO getUserSessionInfo(boolean withError) {

        // 获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 获取请求头Token值
        String token = request == null ? null : request.getHeader(Constants.AUTH_TOKEN_HEADER);

        // 从求头中提取 Session 信息，并返回
        return SessionUtil.getUserSessionInfo(token, withError);
    }

    /**
     * 获取用户Session信息
     * @param token
     * @param withError
     * @return
     */
    public static UserSessionVO getUserSessionInfo(String token, boolean withError) {

        // 参数有效性校验
        if (withError && (StringUtils.isBlank(token))) {
            ExceptionUtil.rollback("登录已过期，请重新登录！", ErrorConstants.LOGIN_TIMEOUT);
        }
        // 获取UserSession
        UserSessionVO userSessionVO = (UserSessionVO) RedisUtil.get(StringUtil.concat(CacheKeyConstants.USER_SESSION_KEY_TOKEN, ":", token));
        if (withError && userSessionVO == null){
            ExceptionUtil.rollback("登录已过期，请重新登录！", ErrorConstants.LOGIN_TIMEOUT);
    }
        // 返回Session 信息
        return userSessionVO;
    }

    /**
     * 获取用户Session信息
     * @param userId
     * @param client
     * @return
     */
    public static UserSessionVO getUserSessionInfo(Long userId, String client) {

        // 参数有效性校验
        if (userId == null || StringUtils.isBlank(client)) {
            return null;
        }
        // 获取UserSession
        return (UserSessionVO) RedisUtil.get(StringUtil.concat(CacheKeyConstants.USER_SESSION_KEY_USER, client, ":", userId));
    }

    /**
     * 更新用户Session信息
     * @param userSessionVO
     * @return
     */
    public static UserSessionVO updateUserSessionInfo(UserSessionVO userSessionVO) {

        // 获取Token值
        if (userSessionVO == null || StringUtils.isBlank(userSessionVO.getToken())) {
            ExceptionUtil.rollback("用户Session信息不可为空！", ErrorConstants.OPERATION_FAIL);
        }
        // 更新缓存中Session 信息
        RedisUtil.set(StringUtil.concat(CacheKeyConstants.USER_SESSION_KEY_TOKEN, userSessionVO.getClient(), ":", userSessionVO.getToken()), userSessionVO, 1000);
        RedisUtil.set(StringUtil.concat(CacheKeyConstants.USER_SESSION_KEY_USER, userSessionVO.getClient(), ":", userSessionVO.getUserId()), userSessionVO, 1000);

        // 返回Session信息
        return userSessionVO;
    }

    /**
     * 删除用户Session信息
     * @param userSessionVO
     */
    public static void deleteUserSessionInfo(UserSessionVO userSessionVO) {

        // 获取Token值
        if (userSessionVO == null || StringUtils.isBlank(userSessionVO.getToken())) {
            ExceptionUtil.rollback("用户Session信息不可为空！", ErrorConstants.OPERATION_FAIL);
        }
        // 删除缓存中Session 信息
        RedisUtil.delete(StringUtil.concat(CacheKeyConstants.USER_SESSION_KEY_TOKEN, userSessionVO.getClient(), ":", userSessionVO.getToken()));
        RedisUtil.delete(StringUtil.concat(CacheKeyConstants.USER_SESSION_KEY_USER, userSessionVO.getClient(), ":", userSessionVO.getUserId()));
    }
}
