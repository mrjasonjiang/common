package com.djangson.common.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BeanTool implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanTool.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取当前服务（应用）名称
     * @return
     */
    public static String getApplicationName() {
        return getProperty("spring.application.name");
    }

    /**
     * 根据Bean名称获取 Bean对象
     * @param
     * @return
     */
    public static Object getBean(String beanName) {
        try {
            return applicationContext.getBean(beanName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据Bean名称获取 Bean对象
     * @param beanName
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        try {
            return applicationContext.getBean(beanName, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据Class获取 Bean对象
     * @param
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception E) {
            return null;
        }
    }

    /**
     * 根据Class获取 Bean对象列表
     * @param
     * @return
     */
    public static <T> List<T> getBeanList(Class<T> clazz) {
        try {
            return new ArrayList<>(applicationContext.getBeansOfType(clazz).values());
        } catch (Exception E) {
            return new ArrayList<>(0);
        }
    }

    /**
     * 根据Class获取 Bean对象
     * @param
     * @return
     */
    public static void registerBean(String beanName, Class clazz, Object... constructorParams) {

        // 创建Bean原始定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if (constructorParams.length > 0) {
            for (Object constructorParam : constructorParams) {
                beanDefinitionBuilder.addConstructorArgValue(constructorParam);
            }
        }

        // 注册Bean
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
    }

    /**
     * 获取配置文件中的属性值
     * @return
     */
    public static String getProperty(String key) {
        try {
            return applicationContext.getEnvironment().getProperty(key);
        } catch (Exception e) {
            return null;
        }
    }
}
