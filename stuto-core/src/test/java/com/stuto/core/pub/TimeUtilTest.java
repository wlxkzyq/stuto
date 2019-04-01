package com.stuto.core.pub;

import org.junit.Test;

import java.time.*;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/14 15:50
 */
public class TimeUtilTest {

    @Test
    public void dateToYmd() throws Exception {
        String dateString = TimeUtil.dateToYmd(LocalDate.now());
        System.out.println(dateString);
    }

    @Test
    public void dateFromYmd() throws Exception {
        String dateString = "2018-09-09";
        LocalDate date = TimeUtil.dateFromYmd(dateString);

        System.out.println(date);
    }

    @Test
    public void timeToHms() throws Exception {
        String timeString = TimeUtil.timeToHms(LocalTime.now());
        System.out.println(timeString);
    }

    @Test
    public void timeFromHms() throws Exception {
        String timeString = "13:22:59";
        LocalTime time = TimeUtil.timeFromHms(timeString);

        System.out.println(time);
    }

    @Test
    public void dateTimeToYmdHms() throws Exception {
        String timeString = TimeUtil.dateTimeToYmdHms(LocalDateTime.now());
        System.out.println(timeString);
    }

    @Test
    public void dateTimeFromYmdHms() throws Exception {
        String timeString = "2008-11-11 13:22:59";
        LocalDateTime time = TimeUtil.dateTimeFromYmdHms(timeString);

        System.out.println(time);

        System.out.println(Instant.now().toEpochMilli());
        System.out.println(Instant.now().toEpochMilli());
    }

    @Test
    public void toEpochMilli() throws Exception {
        Instant instant = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        java.util.Date date = new Date(instant.toEpochMilli());

        long l = TimeUtil.toEpochMilli(localDateTime);
        System.out.println(l);
        System.out.println(date.getTime());
    }

    @Test
    public void fromEpochMilli() throws Exception {
        LocalDateTime localDateTime = TimeUtil.fromEpochMilli(1542188229337l);

        System.out.println(TimeUtil.dateTimeToYmdHms(localDateTime));
    }

    @Test
    public void getWeekOfYear(){
        System.out.println(TimeUtil.getWeekOfYear(LocalDate.now()));
    }
}
