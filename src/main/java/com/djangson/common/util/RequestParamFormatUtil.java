package com.djangson.common.util;

import com.djangson.common.constant.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/3/11 10:18
 */
public class RequestParamFormatUtil {

    /**
     * 请求参数转换
     * @param param
     * @return
     */
    public static Map<String, String> formatRequestParam(Object param) {
        return RequestParamFormatter.requestParamFormat(param);
    }

    /**
     * 请求参数转换
     * @param paramList
     * @return
     */
    public static List<Map<String, String>> formatRequestParam(List<Object> paramList) {
        return RequestParamFormatter.requestParamFormat(paramList);
    }

    /**
     * 请求参数转换
     * @param param
     * @return
     */
    public static String formatGetRequestParam(Object param) {

        // 解析参数
        Map<String, String> paramMap = RequestParamFormatter.requestParamFormat(param);

        // 初始化返回结果对象
        StringBuilder builder = new StringBuilder();

        // 循环遍历参数集，拼接
        paramMap.forEach((key, value) -> {
            builder.append(key).append("=").append(value).append("&");
        });

        // 去除末尾的&，返回
        return builder.length() > 0 ? builder.deleteCharAt(builder.length() - 1).toString() : Constants.BLANK_STRING;
    }

    /**
     * 请求参数转换
     * @param param
     * @return
     */
    public static String formatGetRequestParam(String url, Object param) {

        // 参数转换
        String paramStr = formatGetRequestParam(param);
        if (StringUtils.isBlank(paramStr)) {
            return url;
        }

        // 将URL转换为动态可编辑对象
        StringBuilder builder = new StringBuilder(url);

        // 若URL从未拼接过任何参数则需要拼接？
        if (builder.lastIndexOf("?") < 0) {
            builder.append('?');
        }

        // 若URL已拼接过参数
        char lastChar = builder.charAt(builder.length() - 1);
        if (lastChar != '?' && lastChar != '&') {
            builder.append('&');
        }

        // 参数拼接完成，转换url
        return builder.append(paramStr).toString();
    }

    /**
     * 内部类，请求参数解析器
     */
    private static class RequestParamFormatter {

        private static Map<String, String> requestParamFormat(Object param) {

            // 初始化结果集
            Map<String, String> resultMap = new HashMap<>();

            // 将JAVA对象转换成JSON对象
            Map<String, Object> paramMap = JsonUtil.parseHashMap(JsonUtil.toJsonString(param));

            // 若参数不存在
            if (CollectionUtils.isEmpty(paramMap)) {
                return resultMap;
            }

            // 循环遍历需要解析的参数
            for (Map.Entry entry : paramMap.entrySet()) {
                String prefix = String.valueOf(entry.getKey());
                parseJsonObject(paramMap.get(prefix), resultMap, prefix);
            }

            // 返回结果
            return resultMap;
        }

        /**
         * 通用参数解析
         * @param paramList
         * @return
         */
        private static List<Map<String, String>> requestParamFormat(List<Object> paramList) {

            // 初始化结果集
            List<Map<String, String>> resultList = new ArrayList<>();

            // 若参数不存在
            if (paramList == null || paramList.size() <= 0) {
                return resultList;
            }

            // 循环遍历参数，解析参数
            for (Object object : paramList) {
                resultList.add(requestParamFormat(object));
            }

            // 返回结果
            return resultList;
        }

        /**
         * 通用参数转换器(递归)
         * @param param
         * @param resultMap
         * @param prefix
         */
        private static void parseJsonObject(Object param, Map<String, String> resultMap, String prefix) {

            // 参数校验
            if (param == null) {
                return;
            }

            // 判断元素类型，做相应处理
            if (param instanceof List) {
                List list = (List) param;
                for (int index = 0; index < list.size(); index++) {
                    parseJsonObject(list.get(index), resultMap, new StringBuilder(prefix).append("[").append(index).append("]").toString());
                }
            } else if (param instanceof Map) {
                for (Map.Entry<String, Object> entry : ((Map<String, Object>) param).entrySet()) {
                    parseJsonObject(entry.getValue(), resultMap, new StringBuilder(prefix).append(".").append(entry.getKey()).toString());
                }
            } else if (param instanceof LocalDateTime) {
                resultMap.put(prefix, String.valueOf(((LocalDateTime) param).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            } else if (param instanceof LocalDate) {
                resultMap.put(prefix, String.valueOf(((LocalDate) param).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            } else if (param instanceof Date) {
                resultMap.put(prefix, String.valueOf(((Date) param).getTime()));
            } else {
                resultMap.put(prefix, String.valueOf(param));
            }
        }
    }
}
