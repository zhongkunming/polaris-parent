package com.github.polaris.util;

import cn.hutool.core.date.BetweenFormatter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * @author zhongkunming
 */
public class DateUtil extends cn.hutool.core.date.DateUtil {

    /**
     * 格式化日期间隔输出
     *
     * @param start 起始日期
     * @param end   结束日期
     * @return XX天XX小时XX分XX秒
     */
    public static String pettyBetween(Date start, Date end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return null;
        }
        return formatBetween(start, end, BetweenFormatter.Level.SECOND);
    }

    /**
     * 格式化日期间隔输出
     *
     * @param start 起始日期
     * @param end   结束日期
     * @return XX天XX小时XX分XX秒
     */
    public static String pettyBetween(LocalDateTime start, LocalDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return null;
        }
        return pettyBetween(cn.hutool.core.date.DateUtil.date(start).toJdkDate(), cn.hutool.core.date.DateUtil.date(end).toJdkDate());
    }
}
