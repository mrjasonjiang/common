package com.djangson.common.util;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @Description: 反射工具
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/01/10 17:55
 */
public class ReflectUtil extends ReflectionUtils {

    /**
     * 根据Class对象获取构造函数
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Constructor<T> getConstructor(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (Exception e) {
            ExceptionUtil.rollback("获取构造函数失败！", e);
            return null;
        }
    }

    /**
     * 根据类获取实例对象
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getInstanceByClass(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            ExceptionUtil.rollback("获取构造函数失败！", e);
            return null;
        }
    }

    /**
     * 根据构造函数获取实例对象
     * @param constructor
     * @param <T>
     * @return
     */
    public static <T> T getInstanceByConstructor(Constructor<T> constructor) {
        try {
            return constructor.newInstance();
        } catch (Exception e) {
            ExceptionUtil.rollback("获取构造函数失败！", e);
            return null;
        }
    }

    /**
     * 获取默认类加载器
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {

        // 1. 尝试从当前线程上下文中获取类加载器
        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        if (threadClassLoader != null) {
            return threadClassLoader;
        }

        // 2. 尝试获取当前类的加载器
        ClassLoader classLoader = ReflectUtil.class.getClassLoader();
        if (classLoader != null) {
            return classLoader;
        }

        // 3. 获取系统默认加载器
        return ClassLoader.getSystemClassLoader();
    }

    /**
     * 获取系统默认类加载器
     * @param name
     * @return
     */
    public static Class<?> getClassByName(String name) {
        try {
            return Class.forName(name, false, ReflectUtil.getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException ex) {
                throw ExceptionUtils.mpe("找不到指定的class！请仅在明确确定会有 class 的时候，调用该方法", e);
            }
        }
    }

    /**
     * 获取类的属性（字段）对象
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 获取类的属性（字段）上的注解
     * @param clazz
     * @param fieldName
     * @return
     */
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
        Field field = ReflectUtil.getField(clazz, fieldName);
        return field == null ? null : field.getAnnotation(annotationClass);
    }

    /**
     * 判断对象中是否所有字段都为空
     * @param object
     * @return
     */
    public static boolean isAllFieldEmpty(Object object) {
        try {
            Field[] declaredFields = object.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (declaredField.get(object) != null) {
                    return false;
                }
            }
        } catch (Exception e) {
            ExceptionUtil.rollback("Error Occurred While Process...", e);
        }
        return true;
    }

    /**
     * Java对象序列化
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
