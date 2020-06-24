package com.djangson.common.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.Map;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/10/25 11:11
 */
public class TemplateRenderUtil {

    /**
     * Html页面模板渲染
     * @param htmlContent
     * @param variableMap
     * @return
     */
    public static String renderingHtml(String htmlContent, Map<String, Object> variableMap) {
        try {
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateLoader(new StringTemplateLoader());

            StringWriter stringWriter = new StringWriter();
            Template template = new Template("", htmlContent, configuration);
            template.process(variableMap, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            ExceptionUtil.rollback("模板渲染失败！", e);
            return null;
        }
    }
}
