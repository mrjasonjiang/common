package com.djangson.common.config;

import com.djangson.common.context.converter.WebConverterConfig;
import com.djangson.common.context.converter.WebJacksonConfig;
import com.djangson.common.context.handler.WebMvcExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Servlet;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2020/3/27 16:43
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class WebMvcResolverAutoConfiguration {

    @Bean
    @Primary
    public static ObjectMapper webObjectMapper() {
        return WebJacksonConfig.objectMapper();
    }

    @Bean
    WebConverterConfig webConverterConfigurer() {
        return new WebConverterConfig();
    }

    @Bean
    public WebMvcExceptionHandler getHandlerExceptionResolver() {
        return new WebMvcExceptionHandler();
    }
}
