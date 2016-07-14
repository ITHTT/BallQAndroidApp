package com.tysci.ballq.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016-07-14 0014.
 */
public final class DateFormatUtil {
    public static <T> String format(long timeMillis, String format) {
        Calendar c=Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            return sdf.format(new Date(timeMillis));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }
}
