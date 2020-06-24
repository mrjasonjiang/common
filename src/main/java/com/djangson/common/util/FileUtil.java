package com.djangson.common.util;

import com.djangson.common.enums.SystemImageFormat;
import com.djangson.common.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ResourceUtils;

/**
 * @Author: wangqinjun@vichain.com
 * @Description:
 * @date : 2019/6/19 09:09
 */
public class FileUtil {

    /**
     * 获取当前项目部署路径
     * @return
     */
    public static String getRootPath() {
        try {
            return ResourceUtils.getURL("classpath:").getPath();
        } catch (Exception e) {
            throw new BusinessException("获取项目部署路径失败！", e);
        }
    }

    /**
     * 获取文件扩展名
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {

        // 参数校验
        if (StringUtils.isBlank(fileName)) {
            return "";
        }

        // 获取文件路径分隔符位置
        int fileSeparatorIndex = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        // 获取扩展名分隔符的位置
        int extensionIndex = fileName.lastIndexOf(".");

        // 返回文件扩展名
        return extensionIndex <= fileSeparatorIndex ? "" : fileName.substring(extensionIndex);
    }

    /**
     * 根据文件后缀判断是否图片
     * @param extension
     * @return
     */
    public static boolean isImage(String extension) {
        for (SystemImageFormat imageFormat : SystemImageFormat.values()) {
            if (extension.replace(".", "").equalsIgnoreCase(imageFormat.getValue())) {
                return true;
            }
        }
        return false;
    }
}
