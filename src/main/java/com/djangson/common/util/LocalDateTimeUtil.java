package com.djangson.common.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间工具类
 * @Author: wangqinjun@vichain.com
 * @Date 2018/09/20 10:00
 */
public class LocalDateTimeUtil {

    public static final LocalTime LOCAL_TIME_MAX = LocalTime.of(23, 59, 59);

    /**
     * 获取当前时间
     * @return
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

    /**
     * 获取当前时间，按传入格式返回
     * @return
     */
    public static String getCurrentLocalDateTime(String dateFormatStr) {
        return LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(dateFormatStr));
    }

    /**
     * 将日期格式化为指定格式的日期字符串
     * @param localDateTime
     * @param dateFormatStr
     * @return
     */
    public static String formatDate(LocalDateTime localDateTime, String dateFormatStr) {
        return localDateTime == null ? null : DateTimeFormatter.ofPattern(dateFormatStr).format(localDateTime);
    }

    /**
     * 将符合格式的日期字符串序列化为日期对象
     * @param dateStr
     * @param dateFormatStr
     * @return
     */
    public static LocalDateTime parseDate(String dateStr, String dateFormatStr) {
        return DateUtil.toLocalDateTime(DateUtil.parseDate(dateStr, dateFormatStr));
    }

    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当天开始时间
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getStartTimeOfDay(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
    }

    /**
     * 获取当天结束时间
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getEndTimeOfDay(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalDateTimeUtil.LOCAL_TIME_MAX);
    }

    /**
     * 清除时间中的毫秒值
     * @param localDateTime
     * @return
     */
    public static LocalDateTime clearMillSecond(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.of(localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()));
    }
}
