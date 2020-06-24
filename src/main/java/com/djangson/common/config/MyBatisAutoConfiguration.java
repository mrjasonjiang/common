package com.djangson.common.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.djangson.common.mybatis.injector.BaseLogicSqlInjector;
import com.djangson.common.mybatis.interceptor.KeywordEscapeInterceptor;
import com.djangson.common.mybatis.interceptor.PaginationInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2020/3/27 16:43
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@EnableConfigurationProperties({MybatisPlusProperties.class})
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
public class MyBatisAutoConfiguration {

    @Bean
    public ISqlInjector sqlInjector() {
        return new BaseLogicSqlInjector();
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public KeywordEscapeInterceptor keywordEscapeInterceptor() {
        return new KeywordEscapeInterceptor();
    }
}
