package com.djangson.common.enums;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/2/14 14:40
 */
public enum SystemLanguageEnum {

    ZH("zh", "中文"), EN("en", "英文");

    SystemLanguageEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private final String code;
    private final String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
