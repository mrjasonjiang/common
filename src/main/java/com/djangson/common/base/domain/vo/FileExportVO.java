package com.djangson.common.base.domain.vo;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/11/26 10:56
 */
public class FileExportVO {

    private String cacheCode;
    private Integer exportRate;
    private Integer exportStatus;
    private Long fileId;
    private String fileCode;

    public String getCacheCode() {
        return cacheCode;
    }

    public FileExportVO setCacheCode(String cacheCode) {
        this.cacheCode = cacheCode;
        return this;
    }

    public Integer getExportRate() {
        return exportRate;
    }

    public FileExportVO setExportRate(Integer exportRate) {
        this.exportRate = exportRate;
        return this;
    }

    public Integer getExportStatus() {
        return exportStatus;
    }

    public FileExportVO setExportStatus(Integer exportStatus) {
        this.exportStatus = exportStatus;
        return this;
    }

    public Long getFileId() {
        return fileId;
    }

    public FileExportVO setFileId(Long fileId) {
        this.fileId = fileId;
        return this;
    }

    public String getFileCode() {
        return fileCode;
    }

    public FileExportVO setFileCode(String fileCode) {
        this.fileCode = fileCode;
        return this;
    }
}
