package com.djangson.common.context.handler;

import com.djangson.common.base.domain.Result;
import com.djangson.common.base.domain.vo.UserSessionVO;
import com.djangson.common.constant.ErrorConstants;
import com.djangson.common.exception.BusinessException;
import com.djangson.common.util.ErrorMessageUtil;
import com.djangson.common.util.JsonUtil;
import com.djangson.common.util.LoggerUtil;
import com.djangson.common.util.SessionUtil;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Description: 系统统一异常处理
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/08/20 18:49
 */
public class WebMvcExceptionHandler implements org.springframework.web.servlet.HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {

        LoggerUtil.error("系统统一异常处理：", exception);

        // 若响应已响应或已关闭，则不操作
        if (response.isCommitted()) {
            return new ModelAndView();
        }

        // 获取Session 信息
        UserSessionVO userSessionVO = SessionUtil.getUserSessionInfo(false);
        String language = userSessionVO == null ? null : userSessionVO.getCurrentLanguage();

        // 组装错误提示信息
        String errorCode = exception instanceof BusinessException ? ((BusinessException) exception).getCode() : ErrorConstants.OPERATION_FAIL;
        String message = ErrorMessageUtil.getErrorMessage(errorCode, language);

        // 响应类型设置
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 响应结果输出
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(JsonUtil.toJsonString(Result.createWithErrorMessage(message, errorCode)));
        } catch (Exception e) {
            LoggerUtil.error("响应输出失败！原因如下：", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        return new ModelAndView();
    }
}
