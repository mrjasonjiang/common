package com.djangson.common.util;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/3/22 09:25
 */
public class StringUtil {

    /**
     * 字符串拼接
     * @param strings
     * @return
     */
    public static String concat(String... strings) {

        // 初始化结果集
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : strings) {
            if (!isEmpty(str)) {
                stringBuilder.append(str);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 字符串拼接
     * @param objects
     * @return
     */
    public static String concat(Object... objects) {

        // 初始化结果集
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0, length = objects.length; index < length; index++) {
            String str = objects[index] == null ? null : objects[index].toString();
            if (!isEmpty(str)) {
                stringBuilder.append(str);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 判断字符串是否有效
     * @param str
     * @return
     */
    public static String valueOf(Object str) {
        return str == null ? null : str.toString();
    }

    /**
     * 判断字符串是否有效
     * @param str
     * @return
     */
    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
