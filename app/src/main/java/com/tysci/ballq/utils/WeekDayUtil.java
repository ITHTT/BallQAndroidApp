package com.tysci.ballq.utils;

/**
 * Created by HTT on 2016/6/18.
 */
public class WeekDayUtil {
    //    private final static String[] week_zh_simple = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private final static String[] week_zh = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};

    public static String getZhWeekDay(int day)
    {
        try
        {
            return getWeekZh(day);
        } catch (NumberFormatException e)
        {
            return "";
        }
    }

    public static String getWeekZh(int i)
    {
        return getWeekZh(i, false);
    }

    public static String getWeekZh(int i, boolean startFrom0)
    {
        try
        {
            return startFrom0 ? week_zh[i] : week_zh[i - 1];
        } catch (Exception e)
        {
            return "";
        }
    }
}
