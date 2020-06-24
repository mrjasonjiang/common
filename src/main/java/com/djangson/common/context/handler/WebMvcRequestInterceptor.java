//package com.djangson.common.context.handler;
//
//import com.djangson.common.constant.CacheKeyConstants;
//import com.djangson.common.constant.Constants;
//import com.djangson.common.context.BeanTool;
//import com.djangson.common.exception.BusinessException;
//import com.djangson.common.util.LoggerUtil;
//import com.djangson.common.util.RedisUtil;
//import com.djangson.common.util.StringUtil;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.MDC;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @Description: 系统请求重试拦截
// * @Author: wangqinjun@vichain.com
// * @Date: 2018/01/28 18:49
// */
//public class WebMvcRequestInterceptor implements HandlerInterceptor {
//
//    private Boolean retryStrategy = null;
//    private String applicationName = BeanTool.getApplicationName();
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
//
//        // 获取请求流水号
//        String requestId = request.getHeader(Constants.REQUEST_ID_HEADER);
//
//        // 对于非GET请求，则需要保证只能执行一次
//        if (getRetryStrategy() && !request.getMethod().toUpperCase().equals("GET")) {
//
//            // 获取REDIS 缓存响应头
//            String serviceNameKey = StringUtil.concat(CacheKeyConstants.REQUEST_RETRY_KEY, applicationName, ":", requestId);
//
//            // 若请求流水号或者服务名称不存在，则忽略本次请求
//            if (StringUtils.isBlank(applicationName) || StringUtils.isBlank(requestId)) {
//                LoggerUtil.info("请求重试被拒绝！", new BusinessException("请求重试被拒绝！"));
//                return false;
//            }
//
//            // 先查询Redis中是否有此RequestId，若有则忽略本次请求
//            Integer hasHandle = (Integer) RedisUtil.get(serviceNameKey);
//            if (hasHandle != null && hasHandle == Constants.YES) {
//                LoggerUtil.info("请求重试被拒绝！", new BusinessException("请求重试被拒绝！"));
//                return false;
//            }
//
//            // 若没有，则加入 Redis 缓存请求操作记录
//            RedisUtil.set(serviceNameKey, Constants.YES, Constants.REQUEST_RETRY_DURATION);
//        }
//
//        // MDC中缓存请求流水号
//        MDC.put(Constants.REQUEST_ID_HEADER, requestId);
//
//        // 放行请求
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception e) throws Exception {
//
//        // 释放本次请求对象
//        MDC.remove(Constants.REQUEST_ID_HEADER);
//    }
//
//    /**
//     * 获取重试拦截策略，默认不开启拦截
//     * @return
//     */
//    private boolean getRetryStrategy() {
//
//        // 若重试策略已初始化，直接返回
//        if (retryStrategy != null) {
//            return retryStrategy;
//        }
//
//        // 初始化重试策略
//        try {
//            return retryStrategy = Boolean.valueOf(BeanTool.getProperty("retry-strategy"));
//        } catch (Exception e) {
//            return retryStrategy = false;
//        }
//    }
//}
