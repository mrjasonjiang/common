package com.djangson.common.base.domain.vo;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;

/**
 * 公共文件对象
 * @Author: wangqinjun@vichain.com
 * @since 2018-09-28
 */
public class CommonFileVO extends BaseVO<CommonFileVO> {

    @FieldDesc("文件校验码")
    private String code;

    @FieldDesc("原始文件名")
    private String originalName;

    @FieldDesc("文件扩展名")
    private String extend;

    @FieldDesc("文件大小")
    private Long size;

    public String getCode() {
        return code;
    }

    public CommonFileVO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getOriginalName() {
        return originalName;
    }

    public CommonFileVO setOriginalName(String originalName) {
        this.originalName = originalName;
        return this;
    }

    public String getExtend() {
        return extend;
    }

    public CommonFileVO setExtend(String extend) {
        this.extend = extend;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public CommonFileVO setSize(Long size) {
        this.size = size;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
