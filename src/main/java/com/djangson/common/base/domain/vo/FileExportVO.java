package com.djangson.common.base.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/11/26 10:56
 */
@Getter
@Setter
@Accessors(chain=true)
public class FileExportVO {

    private String cacheCode;
    private Integer exportRate;
    private Integer exportStatus;
    private Long fileId;
    private String fileCode;

}
