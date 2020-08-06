package com.djangson.common.base.domain.vo;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 公共文件对象
 * @Author: wangqinjun@vichain.com
 * @since 2018-09-28
 */
@Getter
@Setter
@Accessors(chain=true)
public class CommonFileVO extends BaseVO<CommonFileVO> {

    @FieldDesc("文件校验码")
    private String code;

    @FieldDesc("原始文件名")
    private String originalName;

    @FieldDesc("文件扩展名")
    private String extend;

    @FieldDesc("文件大小")
    private Long size;

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
