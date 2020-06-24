package com.djangson.common.util;

import com.djangson.common.constant.Constants;
import com.djangson.common.constant.ErrorConstants;
import com.djangson.common.enums.SystemDateFormatEnum;

import java.util.Random;
import java.util.UUID;

public class KeyGeneratorUtil {

    /**
     * 生成流水号
     * @return
     */
    public static synchronized String createSerialNumber() {
        return new StringBuilder().append(System.currentTimeMillis()).append(System.nanoTime()).toString();
    }

    /**
     * 生成流水号
     * @return
     */
    public static synchronized String createSerialNumberForTime() {
        return new StringBuilder().append(DateUtil.getCurrentTime(SystemDateFormatEnum.DATE_TIME_FORMAT_MILL.getValue())).append(System.nanoTime()).toString();
    }

    /**
     * 生成用于不带-的UUID
     * @return
     */
    public static synchronized String createUUID() {
        return UUID.randomUUID().toString().replaceAll("-", Constants.BLANK_STRING);
    }

    /**
     * 生成用于表示session的Token
     * @return
     */
    public static String createSessionToken() {
        return createUUID();
    }

    /**
     * 生成系统默认账号
     * @return
     */
    public static String createUserAccount(String prefix) {
        return StringUtil.concat(prefix, createUUID());
    }

    /**
     * 生成可变位数随机码
     * @return
     */
    public static String createRandomNumberCode(int capacity) {

        if (capacity < 0 || capacity > 1024) {
            ExceptionUtil.rollback("随机码位数超出支持范围", ErrorConstants.OPERATION_FAIL);
        }

        Random random = new Random();
        StringBuilder code = new StringBuilder(capacity);
        for (int i = 0; i < capacity; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }
}
