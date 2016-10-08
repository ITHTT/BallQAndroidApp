package com.tysci.ballq.utils;

import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by LinDe
 * on 2016-08-19 0019.
 */
public final class ParseUtil
{
    /**
     * @param s        String
     * @param defValue T
     * @param <T>      may be int long float double etc.
     * @return T
     */
    @SuppressWarnings("unchecked")
    private static <T> T stringParseNumber(String s, T defValue)
    {
        if (TextUtils.isEmpty(s) || defValue == null)
            return defValue;
        if (defValue instanceof Integer)
        {
            Integer i;
            try
            {
                i = Integer.parseInt(s);
            } catch (NumberFormatException e)
            {
                i = 0;
            }
            return (T) i;
        } else if (defValue instanceof Long)
        {
            Long l;
            try
            {
                l = Long.parseLong(s);
            } catch (NumberFormatException e)
            {
                l = 0L;
            }
            return (T) l;
        } else if (defValue instanceof Float)
        {
            Float f;
            try
            {
                f = Float.parseFloat(s);
            } catch (NumberFormatException e)
            {
                f = 0F;
            }
            return (T) f;
        } else if (defValue instanceof Double)
        {
            Double d;
            try
            {
                d = Double.parseDouble(s);
            } catch (NumberFormatException e)
            {
                d = 0D;
            }
            return (T) d;
        }

        return defValue;
    }

    public static <Parse, Return> Return makeParse(Parse parse, Return defValue) throws ClassCastException
    {
        if (parse == null || defValue == null)
            return defValue;

        Class parseClass = parse.getClass();
        Class defValueClass = defValue.getClass();

        //noinspection unchecked
        if (parseClass == defValueClass || defValueClass.isAssignableFrom(parseClass))
        {
            //noinspection unchecked
            return (Return) parse;
        }

        if (defValue instanceof String)
        {
            //noinspection unchecked
            return (Return) parse.toString();
        }

        if (parse instanceof String)
        {
            return makeParse(parse.toString(), defValue);
        }

        try
        {
            //noinspection unchecked
            return (Return) parse;
        } catch (ClassCastException e)
        {
            e.printStackTrace();
        }

        return defValue;
    }

    private static <Return> Return makeParse(String parse, Return defValue)
    {
        if (TextUtils.isEmpty(parse))
        {
            return defValue;
        }
        // int long float double 转化
        if (defValue instanceof Integer || defValue instanceof Long || defValue instanceof Float || defValue instanceof Double)
        {
            return stringParseNumber(parse, defValue);
        }
        // boolean 转化
        if (defValue instanceof Boolean)
        {
            Boolean b;
            switch (parse.toLowerCase().replaceAll(" ", ""))
            {
                case "true":
                    b = true;
                    break;
                case "false":
                default:
                    b = false;
                    break;
            }
            //noinspection unchecked
            return (Return) b;
        }

        return defValue;
    }

    /**
     * 处理小数，保留指定位数的小数，并去掉末位的0
     *
     * @param d             待处理小数
     * @param decimalLength 小数点位数（最高为10）
     * @return {@link String}
     */
    public static String handlerDecimal(double d, int decimalLength)
    {
        if (d == 0) return String.valueOf(0);

        if (decimalLength < 0) decimalLength = 0;
        else if (decimalLength > 10) decimalLength = 10;

        int point;
        for (int i = 0, baseNumber = 10; i <= decimalLength; i++)
        {
            point = (int) Math.pow(baseNumber, i);
            if ((d * point) % 1 == 0)
            {
                return String.format(Locale.getDefault(), "%." + i + "f", d);
            }
        }
        return String.format(Locale.getDefault(), "%." + decimalLength + "f", d);
    }
}
