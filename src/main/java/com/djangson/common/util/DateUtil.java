package com.djangson.common.util;

import com.djangson.common.enums.SystemDateFormatEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 时间工具类
 * @Author: wangqinjun@vichain.com
 * @Date 2018/09/20 10:00
 */
public class DateUtil {

    private static final int DAY_MILLS = 1000 * 3600 * 24;

    private static final Map<String, FastDateFormat> dateFormatMap = new HashMap<>();

    static {
        dateFormatMap.put(SystemDateFormatEnum.DATE_FORMAT_MONTH.getValue(), FastDateFormat.getInstance(SystemDateFormatEnum.DATE_FORMAT_MONTH.getValue()));
        dateFormatMap.put(SystemDateFormatEnum.DATE_FORMAT_NORMAL.getValue(), FastDateFormat.getInstance(SystemDateFormatEnum.DATE_FORMAT_NORMAL.getValue()));
        dateFormatMap.put(SystemDateFormatEnum.DATE_FORMAT_DIAGONAL.getValue(), FastDateFormat.getInstance(SystemDateFormatEnum.DATE_FORMAT_DIAGONAL.getValue()));
        dateFormatMap.put(SystemDateFormatEnum.DATE_FORMAT_NO_SEPARATOR.getValue(), FastDateFormat.getInstance(SystemDateFormatEnum.DATE_FORMAT_NO_SEPARATOR.getValue()));
        dateFormatMap.put(SystemDateFormatEnum.DATE_TIME_FORMAT_NORMAL.getValue(), FastDateFormat.getInstance(SystemDateFormatEnum.DATE_TIME_FORMAT_NORMAL.getValue()));
        dateFormatMap.put(SystemDateFormatEnum.DATE_TIME_FORMAT_NO_SEPARATOR.getValue(), FastDateFormat.getInstance(SystemDateFormatEnum.DATE_TIME_FORMAT_NO_SEPARATOR.getValue()));
        dateFormatMap.put(SystemDateFormatEnum.DATE_TIME_FORMAT_MILL.getValue(), FastDateFormat.getInstance(SystemDateFormatEnum.DATE_TIME_FORMAT_MILL.getValue()));
        dateFormatMap.put(SystemDateFormatEnum.DATE_TIME_FORMAT_GMT.getValue(), FastDateFormat.getInstance(SystemDateFormatEnum.DATE_TIME_FORMAT_GMT.getValue(), Locale.US));
    }

    /**
     * 获取当前时间
     * @return
     */
    public static Date getCurrentTime() {
        return new Date();
    }

    /**
     * 获取当前时间，按传入格式返回
     * @return
     */
    public static String getCurrentTime(String dateFormatStr) {
        return dateFormatMap.get(dateFormatStr).format(new Date());
    }

    /**
     * 将日期格式化为指定格式的日期字符串
     * @param date
     * @param dateFormatStr
     * @return
     */
    public static String formatDate(Date date, String dateFormatStr) {
        return date == null ? null : dateFormatMap.get(dateFormatStr).format(date);
    }

    /**
     * 将符合格式的日期字符串序列化为日期对象
     * @param dateStr
     * @param dateFormatStr
     * @return
     */
    public static Date parseDate(String dateStr, String dateFormatStr) {
        try {
            return StringUtils.isBlank(dateStr) ? null : new SimpleDateFormat(dateFormatStr).parse(dateStr);
        } catch (Exception e) {
            ExceptionUtil.rollback("日期转换异常！", e);
            return null;
        }
    }

    /**
     * 将时间戳字符串转换为日期对象
     * @param timestampStr
     * @return
     */
    public static Date parseDate(String timestampStr) {
        return new Date(Long.valueOf(timestampStr));
    }

    /**
     * Date转换为LocalDateTime
     * @param date
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将当前日期往后或者往前推一段时间
     * @param date
     * @param count
     * @param calendarType 时间类型
     * @return
     */
    public static Date addTime(Date date, int count, int calendarType) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarType, count);
        return calendar.getTime();
    }

    /**
     * 将当前日期往后或者往前推几天
     * @param date
     * @param count
     * @return
     */
    public static Date addDay(Date date, int count) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, count);
        return calendar.getTime();
    }

    /**
     * 将当前日期往后或者往前推几个月份
     * @param date
     * @param count
     * @return
     */
    public static Date addMonth(Date date, int count) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, count);
        return calendar.getTime();
    }

    /**
     * 计算两个日期之间的差值(天数)
     * @param startDate
     * @param endDate
     * @return
     */
    public static int diffBetweenDays(Date startDate, Date endDate) {
        startDate = startDate == null ? new Date() : startDate;
        endDate = endDate == null ? new Date() : endDate;
        return (int) ((endDate.getTime() - startDate.getTime()) / DAY_MILLS);
    }

    /**
     * 计算两个时间月差值（月份；不论天，只算所占的月份）
     * @param startDate
     * @param endDate
     * @return
     */
    public static int diffBetweenMonth(Date startDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int yearStart = calendar.get(Calendar.YEAR);
        int monthStart = calendar.get(Calendar.MONTH);

        calendar.setTime(endDate);
        int yearEnd = calendar.get(Calendar.YEAR);
        int monthEnd = calendar.get(Calendar.MONTH);

        return (yearEnd - yearStart) * 12 + (monthEnd - monthStart + 1);
    }

    /**
     * 判断日期是否在时间段之内
     * @param curDate 要比较的时间
     * @param start   开始时间
     * @param end     结束时间
     * @return true在时间段内，false不在时间段内
     */
    public static boolean isBetweenDate(Date curDate, Date start, Date end) {
        return !curDate.before(start) && !curDate.after(end);
    }

    /**
     * 判断日期是否在时间段之内
     * @param curDate 要比较的时间
     * @param start   开始时间
     * @param end     结束时间
     * @return true在时间段内，false不在时间段内
     */
    public static boolean isBetweenByYearMonth(Date curDate, Date start, Date end) {
        curDate = DateUtil.getYearMonth(curDate);
        start = DateUtil.getYearMonth(start);
        end = DateUtil.getYearMonth(end);
        return !curDate.before(start) && !curDate.after(end);
    }

    /**
     * 格式化日期为 Y年-M月-1日
     * @param date
     * @return
     */
    public static Date getYearMonth(Date date) {
        return getFirstDayOfMonth(date);
    }

    /**
     * 格式化日期为 Y年-M月-D日
     * @param date
     * @return
     */
    public static Date getYearMonthDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取本月开始日期
     * @return String
     **/
    public static Date getFirstDayOfMonth() {
        return getFirstDayOfMonth(new Date());
    }

    /**
     * 获取指定日期所在月的第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取本月最后一天
     * @return String
     **/
    public static Date getLastDayOfMonth() {
        return getLastDayOfMonth(new Date());
    }

    /**
     * 获取指定日期所在月最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    /**
     * 获取本周的第一天
     * @return String
     **/
    public static Date getFirstDayOfWeek() {
        return getFirstDayOfWeek(new Date());
    }

    /**
     * 每周的第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取本周的最后一天
     * @return String
     **/
    public static Date getLastDayOfWeek() {
        return getLastDayOfWeek(new Date());
    }

    /**
     * 每周的最后一天（下周第一天0点）
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    /**
     * 获取本年的第一天
     * @return Date
     **/
    public static Date getFirstDayOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取本年的最后一天
     * @return Date
     **/
    public static Date getLastDayOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    /**
     * 获取较传入日期N天0点
     * @param date
     * @return
     */
    public static Date getNextDayOfZERO(Date date, int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, count);
        return calendar.getTime();
    }
}
