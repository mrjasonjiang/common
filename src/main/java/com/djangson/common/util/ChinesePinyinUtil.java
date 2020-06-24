package com.djangson.common.util;

import com.djangson.common.constant.Constants;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.apache.commons.lang.StringUtils;

/**
 * @Description: 汉语拼音工具类
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/01/10 17:55
 */
public class ChinesePinyinUtil {

    // 拼音转换模板
    private static final HanyuPinyinOutputFormat PINYIN_FORMAT = new HanyuPinyinOutputFormat();

    static {
        formatInit();
    }

    /**
     * 获取汉字的全拼
     * @param sourceStr 输入源
     * @return
     */
    public static String getPinYinFull(String sourceStr) {
        return getPinYin(sourceStr, 128, true);
    }

    /**
     * 获取汉字的全拼
     * @param sourceStr 输入源
     * @return
     */
    public static String getPinYinFull(String sourceStr, int maxCapacity) {
        return getPinYin(sourceStr, maxCapacity, true);
    }

    /**
     * 得到中文首字母，例如"专科" 得到 zk
     * @param sourceStr 中文字符串
     * @return
     */
    public static String getPinYinHead(String sourceStr) {
        return getPinYin(sourceStr, 128, false);
    }

    /**
     * 得到中文首字母，例如"专科" 得到 zk
     * @param sourceStr 中文字符串
     * @return
     */
    public static String getPinYinHead(String sourceStr, int maxCapacity) {
        return getPinYin(sourceStr, maxCapacity, false);
    }

    /**
     * 将字符串转移为ASCII码
     * @param sourceStr 中文字符串
     * @return
     */
    public static String getASCII(String sourceStr) {

        // 参数校验
        if (StringUtils.isBlank(sourceStr)) {
            return Constants.BLANK_STRING;
        }

        // 初始化结果对象
        StringBuilder builder = new StringBuilder();

        // 获取字节数组
        byte[] byteArr = sourceStr.getBytes();
        for (int i = 0; i < byteArr.length; i++) {
            builder.append(Integer.toHexString(byteArr[i] & 0xff));
        }

        // 返回结果
        return builder.toString();
    }

    /**
     * 拼音转换模板初始化
     */
    private static void formatInit() {
        PINYIN_FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        PINYIN_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        PINYIN_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    /**
     * 获取字符的汉语拼音数据，异常则返回NULL
     * @param character
     * @return
     */
    private static String[] getPinYinArray(char character) {
        try {
            return PinyinHelper.toHanyuPinyinStringArray(character, PINYIN_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取汉字的拼音，支持全拼或首字母
     * @param sourceStr 输入源
     * @return
     */
    private static String getPinYin(String sourceStr, int maxCapacity, boolean full) {

        // 参数校验
        if (StringUtils.isBlank(sourceStr)) {
            return Constants.BLANK_STRING;
        }
        if (maxCapacity < 0 || maxCapacity > 1024) {
            ExceptionUtil.rollback("字符长度超出支持范围！");
        }

        // 初始化结果对象
        StringBuilder builder = new StringBuilder();

        // 开始转换
        for (int i = 0; i < sourceStr.length(); i++) {

            // 取出当前字符
            char character = sourceStr.charAt(i);

            // 判断是否为汉字字符
            boolean isChinese = character >= 0x4E00 && character <= 0x9FA5;

            // 转换
            builder.append(isChinese ? full ? getPinYinArray(character)[0] : getPinYinArray(character)[0].charAt(0) : character);

            // 若超出字符限制，则停止本次操作
            if (builder.length() >= maxCapacity) {
                break;
            }
        }

        // 返回结果
        return builder.length() > maxCapacity ? builder.substring(0, maxCapacity).toString() : builder.toString();
    }
}

