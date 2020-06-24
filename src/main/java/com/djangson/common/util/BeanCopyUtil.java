package com.djangson.common.util;

import com.djangson.common.context.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: Java对象拷贝工具
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/01/10 17:55
 */
public final class BeanCopyUtil {

    /**
     * 对象转换
     * @param resource
     * @param clazz
     * @param <E>
     * @param <T>
     * @return
     */
    public static <E, T> T copy(E resource, Class<T> clazz) {
        T target = ReflectUtil.getInstanceByClass(clazz);
        Converter converter = BeanCopier.createConverter(resource.getClass(), clazz);
        BeanCopierCache.getBeanCopier(resource.getClass(), clazz).copy(resource, target, converter);
        return target;
    }

    /**
     * 对象转换
     * @param resource
     * @param clazz
     * @param <E>
     * @param <T>
     * @return
     */
    public static <E, T> T deepCopy(E resource, Class<T> clazz) {
        return JsonUtil.parseObject(JsonUtil.toJsonString(resource), clazz);
    }

    /**
     * 对象转换
     * @param list
     * @param clazz
     * @param <E>
     * @param <T>
     * @return
     */
    public static <E, T> List<T> copy(List<E> list, Class<T> clazz) {

        // 1. 若原数组为空，则返回空数组
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }

        // 2. 获取输入源类型
        Class resourceType = list.get(0).getClass();

        // 2. 执行对象拷贝
        List<T> resultList = new ArrayList<>(list.size());
        Constructor<T> constructor = ReflectUtil.getConstructor(clazz);
        Converter converter = BeanCopier.createConverter(resourceType, clazz);
        BeanCopier beanCopier = BeanCopierCache.getBeanCopier(resourceType, clazz);
        for (E resource : list) {
            T target = ReflectUtil.getInstanceByConstructor(constructor);
            beanCopier.copy(resource, target, converter);
            resultList.add(target);
        }

        return resultList;
    }

    /**
     * 对象转换
     * @param list
     * @param clazz
     * @param <E>
     * @param <T>
     * @return
     */
    public static <E, T> List<T> deepCopy(List<E> list, Class<T> clazz) {
        return JsonUtil.parseList(JsonUtil.toJsonString(list), clazz);
    }

    /**
     * 对象拷贝缓存类，最多缓存8000个
     */
    private static class BeanCopierCache {

        // 初始化BeanCopier缓存池
        private static final Map<String, BeanCopier> beanCopierCache = new ConcurrentHashMap<>();

        /**
         * 根据源class、目标class获取BeanCopier对象
         * @param resourceClass
         * @param targetClass
         * @return
         */
        private static BeanCopier getBeanCopier(Class resourceClass, Class targetClass) {
            String beanCopierKey = StringUtil.concat(resourceClass.getName(), ";", targetClass.getName());
            BeanCopier beanCopier = beanCopierCache.get(beanCopierKey);
            if (beanCopier == null) {
                beanCopier = BeanCopier.create(resourceClass, targetClass);
                if (beanCopierCache.size() < 8000) {
                    beanCopierCache.put(beanCopierKey, beanCopier);
                }
            }
            return beanCopier;
        }
    }
}
