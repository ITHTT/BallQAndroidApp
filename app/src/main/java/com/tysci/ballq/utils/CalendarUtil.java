package com.tysci.ballq.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by LinDe on 2016-07-13 0013.
 * 日期
 */
public final class CalendarUtil {
    public static final int A_SECOND_MILLIS;
    public static final int A_MINUTE_MILLIS;
    public static final int A_HOUR_MILLIS;
    public static final int A_DAY_MILLIS;

    public final long timeMillis;

    public final int year;
    public final int month;
    public final int day;
    public final int hour;
    public final int minute;
    public final int second;

    static {
        A_SECOND_MILLIS = 1000;
        A_MINUTE_MILLIS = A_SECOND_MILLIS * 60;
        A_HOUR_MILLIS = A_MINUTE_MILLIS * 60;
        A_DAY_MILLIS = A_HOUR_MILLIS * 24;
    }

    public CalendarUtil() {
        this(System.currentTimeMillis());
    }

    public CalendarUtil(long timeMillis) {
        this(timeMillis, TimeZone.getDefault());
    }

    public CalendarUtil(long timeMillis, TimeZone timeZone) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTimeZone(timeZone);
        cal.setTimeInMillis(timeMillis);

        this.timeMillis = timeMillis;
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);
    }

    public CalendarUtil(int year, int month, int day) {
        this(year, month, day, 0, 0, 0);
    }

    public CalendarUtil(int year, int month, int day, int hour, int minute) {
        this(year, month, day, hour, minute, 0);
    }

    public CalendarUtil(int year, int month, int day, int hour, int minute, int second) {
        final Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - 1, day, hour, minute, second);
        cal.setTimeZone(TimeZone.getDefault());

        this.timeMillis = cal.getTimeInMillis();
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH) + 1;
        this.day = cal.get(Calendar.DAY_OF_MONTH);
        this.hour = cal.get(Calendar.HOUR_OF_DAY);
        this.minute = cal.get(Calendar.MINUTE);
        this.second = cal.get(Calendar.SECOND);
    }

    public static
    @Nullable
    CalendarUtil parseStringTZ(String tz) {
        return parseStringTZ(tz, TimeZone.getTimeZone("+GMT"));
    }

    public static
    @Nullable
    CalendarUtil parseStringTZ(String tz, TimeZone timeZone) {
        if (!TextUtils.isEmpty(tz)) {
            if (tz.length() > 20) {
                tz = tz.substring(0, 19) + "Z";
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                sdf.setTimeZone(timeZone);
                final Date d = sdf.parse(tz);
                return new CalendarUtil(d.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getStringFormat(String format) {
        return getStringFormat(format, TimeZone.getDefault());
    }

    /**
     * @param format   格式
     * @param timeZone 时区
     */
    public String getStringFormat(String format, TimeZone timeZone) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTimeInMillis(timeMillis);
        cal.setTimeZone(timeZone);

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            return sdf.format(cal.getTime());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }
}
