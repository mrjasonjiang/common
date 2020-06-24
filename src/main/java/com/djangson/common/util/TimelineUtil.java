package com.djangson.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/6/5 11:52
 */
public class TimelineUtil {

    /**
     * 按天生成时间轴
     * @param start
     * @param end
     * @return
     */
    public static List<Date> generateTimelineDay(Date start, Date end) {
        return generateTimeline(start, end, 0);
    }

    /**
     * 按月生成时间轴
     * @param start
     * @param end
     * @return
     */
    public static List<Date> generateTimelineMonth(Date start, Date end) {
        return generateTimeline(start, end, 1);
    }

    /**
     * 按月生成时间轴
     * @param start
     * @param end
     * @return
     */
    public static List<Date> generateTimelineYear(Date start, Date end) {
        return generateTimeline(start, end, 2);
    }

    /**
     * 按天生成时间轴
     * @param start
     * @param end
     * @return
     */
    public static List<LocalDateTime> generateTimelineDay(LocalDateTime start, LocalDateTime end) {
        return generateTimeline(start, end, 0);
    }

    /**
     * 按月生成时间轴
     * @param start
     * @param end
     * @return
     */
    public static List<LocalDateTime> generateTimelineMonth(LocalDateTime start, LocalDateTime end) {
        return generateTimeline(start, end, 1);
    }

    /**
     * 按月生成时间轴
     * @param start
     * @param end
     * @return
     */
    public static List<LocalDateTime> generateTimelineYear(LocalDateTime start, LocalDateTime end) {
        return generateTimeline(start, end, 2);
    }

    /**
     * 根据起始时间按需生成不同维度时间轴
     * @param start
     * @param end
     * @param timelineType：0：天，1：月，2：年
     * @return
     */
    private static List<Date> generateTimeline(Date start, Date end, int timelineType) {

        // 参数校验
        LocalDateTime startDate = start == null ? LocalDateTime.now() : start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDate = end == null ? LocalDateTime.now() : end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // 生成时间轴
        return generateTimeline(startDate, endDate, timelineType).stream().map(localDateTime -> LocalDateTimeUtil.toDate(localDateTime)).collect(Collectors.toList());
    }

    /**
     * 根据起始时间按需生成不同维度时间轴
     * @param start
     * @param end
     * @param timelineType：0：天，1：月，2：年
     * @return
     */
    private static List<LocalDateTime> generateTimeline(LocalDateTime start, LocalDateTime end, int timelineType) {

        // 参数校验
        start = start == null ? LocalDateTime.now() : start;
        end = end == null ? LocalDateTime.now() : end;

        // 则格式化时间为凌晨、月初、年初
        if (timelineType == 0) {
            start = start.toLocalDate().atStartOfDay();
            end = end.toLocalDate().atStartOfDay();
        } else if (timelineType == 1) {
            start = start.toLocalDate().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
            end = end.toLocalDate().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        } else if (timelineType == 2) {
            start = start.toLocalDate().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
            end = end.toLocalDate().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
        } else {
            start = start.toLocalDate().atStartOfDay();
            end = end.toLocalDate().atStartOfDay();
        }

        // 循环生成时间轴
        List<LocalDateTime> timelineList = new ArrayList<>();
        while (start.isBefore(end) || start.isEqual(end)) {
            timelineList.add(start);
            if (timelineType == 0) {
                start = start.plusDays(1);
            } else if (timelineType == 1) {
                start = start.plusMonths(1);
            } else if (timelineType == 2) {
                start = start.plusYears(1);
            } else {
                start = start.plusDays(1);
            }
        }

        return timelineList;
    }
}
