package com.djangson.common.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class DataTypeValidateUtil {

    /**
     * 是否为整数
     * @param numberStr
     * @return
     */
    public static boolean isInteger(String numberStr) {
        try {
            Integer.valueOf(numberStr);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 匹配是否为数字，支持科学计数法 1.79E3 是合法数据
     * @param numberStr
     * @return
     */
    public static boolean isNumeric(String numberStr) {
        try {
            new BigDecimal(numberStr);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 匹配是否为(正)数字，支持科学计数法 1.79E3 是合法数据
     * @param numberStr
     * @return
     */
    public static boolean isPositiveNumeric(String numberStr) {
        try {
            return new BigDecimal(numberStr).compareTo(BigDecimal.ZERO) > -1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 字符串时间格式校验
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static boolean isDate(String dateStr, String dateFormat) {
        boolean invalidate = true;
        try {
            LocalDateTimeUtil.parseDate(dateStr, dateFormat);
        } catch (Exception e) {
            invalidate = false;
        }
        return invalidate;
    }

    /**
     * 是否是日期
     * @param dateStr
     * @return
     */
    public static boolean isDate(String dateStr) {
        return RegexPattern.DATE_PATTERN.matcher(dateStr).matches();
    }

    /**
     * 是否是邮箱
     * @param emailStr
     * @return
     */
    public static boolean isEmail(String emailStr) {
        return RegexPattern.EMAIL_PATTERN.matcher(emailStr.trim()).matches();
    }

    /**
     * 校验手机号码
     * @param phoneNumber
     * @return
     */
    public static boolean isMobilePhoneNumber(String phoneNumber) {
        return RegexPattern.MOBILE_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * 校验电话号码
     * @param telephoneNumber
     * @return
     */
    public static boolean isTelephoneNumber(String telephoneNumber) {
        return RegexPattern.TEL_PATTERN.matcher(telephoneNumber).matches();
    }


    /**
     * 校验是否是IP 地址
     * @param ip
     * @return
     */
    public static boolean isIP(String ip) {
        return RegexPattern.IP_PATTERN.matcher(ip).matches();
    }

    static class RegexPattern {
        private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-(0?[1-9]|1[0-2])$");
        private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9\u4e00-\u9fa5-_\\.]+@[a-zA-Z0-9_-]+(\\.[A-Za-z0-9\u4e00-\u9fa5]+)+$");
        private static final Pattern MOBILE_PATTERN = Pattern.compile("[1-9]\\d{10}");
        private static final Pattern TEL_PATTERN = Pattern.compile("\\d{3}-\\d{8}|\\d{4}-\\{7,8}");
        private static final Pattern IP_PATTERN = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");
    }
}
