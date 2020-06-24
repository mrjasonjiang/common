package com.djangson.common.util;

import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtil {

    /**
     * 判断List中是否有重复的元素，List中的对象需要覆写equals及hashCode方法
     * @param items
     * @return
     */
    public static boolean hasDuplicateItems(List<? extends Object> items) {
        if (CollectionUtils.isEmpty(items)) {
            return false;
        }
        return new HashSet<>(items).size() != items.size();
    }

    /**
     * 取两个SET集合的差集，T 必须重写hashCode及equals方法
     * @param set1
     * @param set2
     * @param <T>
     * @return
     */
    public static <T> Set<T> difference(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.removeAll(set2);
        return result;
    }

    /**
     * 取两个SET 集合的交集，T 必须重写hashCode及equals方法
     * @param set1
     * @param set2
     * @param <T>
     * @return
     */
    public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<T>(set1);
        result.retainAll(set2);
        return result;
    }
}
