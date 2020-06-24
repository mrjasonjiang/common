package com.djangson.common.context.converter;

import com.djangson.common.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description: 将Json序列化工具设置为FastJson
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/01/15 08:00
 */
public class WebConverterConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

        // 注册时间反序列化转换器，时间戳字符串转换为Date类型
        registry.addConverter(new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                return StringUtils.isBlank(source) ? null : DateUtil.parseDate(source);
            }
        });

        // 注册时间反序列化转换器，时间戳字符串转换为LocalDateTime类型
        registry.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return StringUtils.isBlank(source) ? null : new Timestamp(Long.valueOf(source)).toLocalDateTime().toLocalDate();
            }
        });

        // 注册时间反序列化转换器，时间戳字符串转换为LocalDateTime类型
        registry.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return StringUtils.isBlank(source) ? null : new Timestamp(Long.valueOf(source)).toLocalDateTime();
            }
        });

        // 注册时间反序列化转换器，时间戳转换为LocalDateTime类型
        registry.addConverter(new Converter<Long, LocalDateTime>() {
            @Override
            public LocalDateTime convert(Long source) {
                return source == null ? null : new Timestamp(source).toLocalDateTime();
            }
        });
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注入日志拦截器，用于绑定RequestId
//        registry.addInterceptor(new WebMvcRequestInterceptor());
    }
}
