package com.djangson.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/12/3 09:53
 */
public class LoggerUtil {

    /**
     * 获取日志记录器
     * @return
     */
    public static Logger getLogger() {
        return getDefaultLogger();
    }

    /**
     * 获取Logger对象
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 获取Logger对象
     * @param loggerName
     * @return
     */
    public static Logger getLogger(String loggerName) {
        return LoggerFactory.getLogger(loggerName);
    }

    /**
     * 输出DEBUG级别日志
     * @param message
     * @return
     */
    public static void debug(String message) {
        Logger logger = getDefaultLogger();
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * 输出DEBUG级别日志
     * @param message
     * @param throwable
     * @return
     */
    public static void debug(String message, Throwable throwable) {
        Logger logger = getDefaultLogger();
        if (logger.isDebugEnabled()) {
            logger.debug(message, throwable);
        }
    }

    /**
     * 输出DEBUG级别日志
     * @param message
     * @param objects
     */
    public static void debug(String message, Object... objects) {
        Logger logger = getDefaultLogger();
        if (logger.isDebugEnabled()) {
            logger.debug(message, objects);
        }
    }

    /**
     * 输出INFO级别日志
     * @param message
     * @return
     */
    public static void info(String message) {
        Logger logger = getDefaultLogger();
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    /**
     * 输出INFO级别日志
     * @param message
     * @param throwable
     * @return
     */
    public static void info(String message, Throwable throwable) {
        Logger logger = getDefaultLogger();
        if (logger.isInfoEnabled()) {
            logger.info(message, throwable);
        }
    }

    /**
     * 输出INFO级别日志
     * @param message
     * @param objects
     * @return
     */
    public static void info(String message, Object... objects) {
        Logger logger = getDefaultLogger();
        if (logger.isInfoEnabled()) {
            logger.info(message, objects);
        }
    }

    /**
     * 输出WARNING级别日志
     * @param message
     * @param throwable
     * @return
     */
    public static void warning(String message, Throwable throwable) {
        Logger logger = getDefaultLogger();
        if (logger.isWarnEnabled()) {
            logger.warn(message, throwable);
        }
    }

    /**
     * 输出WARNING级别日志
     * @param message
     * @param objects
     * @return
     */
    public static void warning(String message, Object... objects) {
        Logger logger = getDefaultLogger();
        if (logger.isWarnEnabled()) {
            logger.warn(message, objects);
        }
    }

    /**
     * 输出ERROR级别日志
     * @param message
     * @param throwable
     * @return
     */
    public static void error(String message, Throwable throwable) {
        Logger logger = getDefaultLogger();
        if (logger.isErrorEnabled()) {
            logger.error(message, throwable);
        }
    }

    /**
     * 输出ERROR级别日志
     * @param message
     * @param objects
     * @return
     */
    public static void error(String message, Object... objects) {
        Logger logger = getDefaultLogger();
        if (logger.isErrorEnabled()) {
            logger.error(message, objects);
        }
    }

    /**
     * 根据权限定名获取类
     * @param clazzName
     * @return
     */
    private static Class getClazz(String clazzName) {
        try {
            return Class.forName(clazzName);
        } catch (Exception e) {
            return LoggerUtil.class;
        }
    }

    /**
     * 获取日志记录器
     * @return
     */
    private static Logger getDefaultLogger() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return LoggerFactory.getLogger(getClazz(stackTraceElements[3].getClassName()));
    }
}
