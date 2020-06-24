package com.djangson.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FunctionDesc {

    /**
     * 模块名称
     * @return
     */
    String module() default "";

    /**
     * 操作名称
     * @return
     */
    String operationName() default "";
}
