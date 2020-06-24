package com.djangson.common.util;

import org.apache.commons.lang.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2020/5/15 17:49
 */
public class RequestParamParseUtil {

    /**
     * 解析地址栏请求参数
     * @param url
     * @return
     */
    public static Map<String, Object> parseParam(String url) {

        // 将参数字符串转换为对象格式
        return parseParam(parseParam(url, Charset.defaultCharset()));
    }

    /**
     * 根据编码解析地址栏参数
     * @param url
     * @return
     */
    public static Map<String, String> parseParam(String url, Charset charset) {

        // 参数校验
        if (StringUtils.isBlank(url)) {
            return null;
        }

        // 获取起始下标
        int startIndex = url.indexOf("?");
        if (startIndex < 0) {
            return null;
        }

        // 获取结束下标
        int endIndex = url.indexOf("#", startIndex);
        endIndex = endIndex < 0 ? url.length() : endIndex;

        // 初始化容器结合
        Map<String, String> paramMap = new HashMap<>();

        // 解析参数
        String[] nameValueStrArr = URLDecoder.decode(url.substring(startIndex + 1, endIndex), charset).split("&");
        for (String nameValueStr : nameValueStrArr) {
            String[] nameValueArr = nameValueStr.split("=");
            paramMap.put(nameValueArr[0], nameValueArr.length > 1 ? nameValueArr[1] : null);
        }

        return paramMap;
    }

    /**
     * 将字符串参数转换为对象格式
     * @param paramStringMap
     * @return
     */
    public static Map<String, Object> parseParam(Map<String, String> paramStringMap) {

        // 初始化结果集
        Map<String, Object> resultNode = new HashMap<>();

        // 遍历循环参数，依次转换
        for (Map.Entry<String, String> entry : paramStringMap.entrySet()) {

            // 首次遍历，已根节点作为父节点
            Map<String, Object> parentNode = resultNode;

            // 分割属性名称
            String[] nameArr = entry.getKey().split("\\.");

            // 遍历属性名称，设置属性值
            for (int index = 0, length = nameArr.length, valueIndex = length - 1; index < length; index++) {

                // 1. 取得属性名称
                String fileName = nameArr[index];

                // 2. 判断属性名称是否是数组形式，例如：user[0].name=3
                int startIndex = fileName.indexOf("[");
                if (startIndex > 0) {

                    // 2.1 若满足数组下标解析，则按数组解析，否则按普通字段解析
                    Integer objectIndex = parseInt(fileName.substring(startIndex + 1, fileName.length() - 1));
                    if (objectIndex != null) {

                        // 2.2 当前节点下创建数组节点
                        fileName = fileName.substring(0, startIndex);
                        ArrayList childNodeList = (ArrayList) parentNode.get(fileName);
                        if (childNodeList == null) {
                            parentNode.put(fileName, childNodeList = new ArrayList<>());
                        }

                        // 2.3 若数组节点直接存放数据，则直接解析
                        if (index == valueIndex) {
                            while (childNodeList.size() <= objectIndex) {
                                childNodeList.add(null);
                            }
                            childNodeList.set(objectIndex, entry.getValue());
                            break;
                        }

                        // 2.4 若数组节点为对象类型，则按对象解析
                        if (index != valueIndex) {
                            Object childNode = childNodeList.size() <= objectIndex ? null : childNodeList.get(objectIndex);
                            if (childNode == null) {
                                while (childNodeList.size() <= objectIndex) {
                                    childNodeList.add(null);
                                }
                                childNodeList.set(objectIndex, childNode = new HashMap<String, Object>());
                            }

                            // 将当前节点切换为父节点
                            parentNode = (Map<String, Object>) childNode;
                            continue;
                        }
                    }
                }

                // 3. 若当前节点为value节点，则记录value值
                if (index == valueIndex) {
                    parentNode.put(nameArr[index], entry.getValue());
                    break;
                }

                // 4. 若当前节点为非value节点
                Object childNode = parentNode.get(nameArr[index]);
                if (childNode == null) {
                    parentNode.put(nameArr[index], childNode = new HashMap<String, Object>());
                }

                // 5. 将当前节点切换为父节点
                parentNode = (Map<String, Object>) childNode;
            }
        }

        return resultNode;
    }

    /**
     * 字符串尝试转数字，成功返回数字，失败返回null
     * @param numberStr
     * @return
     */
    private static Integer parseInt(String numberStr) {
        try {
            return Integer.parseInt(numberStr);
        } catch (Exception e) {
            return null;
        }
    }
}
