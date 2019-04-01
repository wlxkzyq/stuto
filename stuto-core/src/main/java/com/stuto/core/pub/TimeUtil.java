package com.stuto.core.pub;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/14 15:46
 */
public class TimeUtil {

    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * localDate 转 字符串形式
     * @param localDate localDate to transfer
     * @return  日期字符串
     */
    public static String dateToYmd(LocalDate localDate){
        return localDate == null ? "" : localDate.format(DATE_FORMATTER);
    }

    /**
     * 字符串解析成localDate
     * @param ymdString 待解析字符串
     * @return  localDate
     */
    public static LocalDate dateFromYmd(String ymdString){
        return StringUtil.isEmpty(ymdString) ? null : LocalDate.parse(ymdString, DATE_FORMATTER);
    }

    /**
     * localTime 转字符串
     * @param localTime localTime to transfer
     * @return  时间字符串
     */
    public static String timeToHms(LocalTime localTime){
        return localTime ==null ? "" : localTime.format(TIME_FORMATTER);
    }

    /**
     * 字符串转localTime
     * @param hmsString 时间字符串
     * @return  LocalTime
     */
    public static LocalTime timeFromHms(String hmsString){
        return StringUtil.isEmpty(hmsString) ? null : LocalTime.parse(hmsString, TIME_FORMATTER);
    }

    /**
     * 日期时间转字符串
     * @param localDateTime localDateTime to transfer
     * @return  日期时间字符串
     */
    public static String dateTimeToYmdHms(LocalDateTime localDateTime){
        return localDateTime ==null ? "" : localDateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * 日期时间字符串转 LocalDateTime
     * @param ymdHmsString  日期时间字符串转
     * @return  LocalDateTime
     */
    public static LocalDateTime dateTimeFromYmdHms(String ymdHmsString){
        return StringUtil.isEmpty(ymdHmsString) ? null : LocalDateTime.parse(ymdHmsString, DATE_TIME_FORMATTER);
    }

    /**
     * localDateTime 转 long型时间戳
     * @param localDateTime localDateTime to transfer
     * @return  long型时间戳
     */
    public static long toEpochMilli(LocalDateTime localDateTime){
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * long型时间戳 转 LocalDateTime
     * @param time  long型时间戳
     * @return  LocalDateTime
     */
    public static LocalDateTime fromEpochMilli(long time){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time),ZoneId.systemDefault());
    }

    /**
     * 获取一年中的第几周
     * @param localDate
     * @return
     */
    public static int getWeekOfYear(LocalDate localDate){
        return localDate.get(WeekFields.ISO.weekOfWeekBasedYear());
    }
}
