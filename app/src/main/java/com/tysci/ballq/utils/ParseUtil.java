package com.tysci.ballq.utils;

import android.text.TextUtils;

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
            }
            catch (NumberFormatException e)
            {
                i = 0;
            }
            return (T) i;
        }
        else if (defValue instanceof Long)
        {
            Long l;
            try
            {
                l = Long.parseLong(s);
            }
            catch (NumberFormatException e)
            {
                l = 0L;
            }
            return (T) l;
        }
        else if (defValue instanceof Float)
        {
            Float f;
            try
            {
                f = Float.parseFloat(s);
            }
            catch (NumberFormatException e)
            {
                f = 0F;
            }
            return (T) f;
        }
        else if (defValue instanceof Double)
        {
            Double d;
            try
            {
                d = Double.parseDouble(s);
            }
            catch (NumberFormatException e)
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
        }
        catch (ClassCastException e)
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
}
