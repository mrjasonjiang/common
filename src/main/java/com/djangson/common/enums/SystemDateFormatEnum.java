package com.djangson.common.enums;

/**
 * 系统日期格式枚举类
 */
public enum SystemDateFormatEnum {

    DATE_FORMAT_MONTH("yyyy-MM"),

    DATE_FORMAT_NORMAL("yyyy-MM-dd"),

    DATE_FORMAT_DIAGONAL("yyyy/MM/dd"),

    DATE_FORMAT_NO_SEPARATOR("yyyyMMdd"),

    DATE_TIME_FORMAT_NORMAL("yyyy-MM-dd HH:mm:ss"),

    DATE_TIME_FORMAT_NO_SEPARATOR("yyyyMMddHHmmss"),

    DATE_TIME_FORMAT_MILL("yyyyMMddHHmmssSSS"),

    DATE_TIME_FORMAT_GMT("EEE dd MMM yyyy HH:mm:ss 'GMT'");

    private final String value;

    SystemDateFormatEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
