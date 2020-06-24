package com.djangson.common.enums;

/**
 * 图片格式枚举类
 */
public enum SystemImageFormat {
    BMP("bmp"), JPG("jpg"), JPEG("jpeg"), PNG("png"), GIF("gif");

    private final String value;

    SystemImageFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
