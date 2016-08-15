package com.tysci.ballq.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Set;

/**
 * Created by Administrator on 2015/12/14.
 */
public class SharedPreferencesUtil
{
    public static final String KEY_TIP_MSG_DOT;
    public static final String KEY_ARTICLE_MSG_DOT;

    private static final String default_file = ".share_cache_data";

    private static final String KEY_NEED_GUIDE;

    private static final String KEY_APP_VERSION_NAME;
    private static final String KEY_APP_VERSION_CODE;

    static
    {
        final String tmp = "SP_UTIL_";
        int i = 0;
        /**
         * 以下顺序不可更改
         */

        // 上次打开版本号描述
        KEY_APP_VERSION_CODE = tmp + (++i);
        // 上次打开版本号
        KEY_APP_VERSION_NAME = tmp + (++i);
        // 引导页打开判断
        KEY_NEED_GUIDE = tmp + (++i);

        // 爆料小红点
        KEY_TIP_MSG_DOT = tmp + (++i);
        // 球茎小红点
        KEY_ARTICLE_MSG_DOT = tmp + (++i);
    }

    public static void setStringValue(Context context, String key, String value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(default_file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setStringValue(Context context, String fileName, String key, String value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringValue(Context context, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(default_file, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public static String getStringValue(Context context, String fileName, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public static void setIntValue(Context context, String key, int value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(default_file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setIntValue(Context context, String fileName, String key, int value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntValue(Context context, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(default_file, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public static int getIntValue(Context context, String fileName, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Context context, String key, T defValue)
    {
        return getValue(context, default_file, key, defValue);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Context context, String spName, String key, T defValue)
    {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        T result = null;
        if (defValue instanceof String)
        {
            String s = sp.getString(key, (String) defValue);
            result = (T) s;
        }
        else if (defValue instanceof Integer)
        {
            Integer i = sp.getInt(key, (Integer) defValue);
            result = (T) i;
        }
        else if (defValue instanceof Float)
        {
            Float f = sp.getFloat(key, (Float) defValue);
            result = (T) f;
        }
        else if (defValue instanceof Long)
        {
            Long l = sp.getLong(key, (Long) defValue);
            result = (T) l;
        }
        else if (defValue instanceof Boolean)
        {
            Boolean b = sp.getBoolean(key, (Boolean) defValue);
            result = (T) b;
        }
        else if (defValue instanceof Set)
        {
            Set<String> set = sp.getStringSet(key, (Set<String>) defValue);
            result = (T) set;
        }
        return result;
    }

    public static <T> boolean setValue(Context context, String key, T value)
    {
        return setValue(context, default_file, key, value);
    }

    public static <T> boolean setValue(Context context, String spName, String key, T value)
    {

        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        boolean putSuccess = true;
        if (value instanceof String)
        {
            editor.putString(key, (String) value);
        }
        else if (value instanceof Integer)
        {
            editor.putInt(key, (Integer) value);
        }
        else if (value instanceof Float)
        {
            editor.putFloat(key, (Float) value);
        }
        else if (value instanceof Long)
        {
            editor.putLong(key, (Long) value);
        }
        else if (value instanceof Boolean)
        {
            editor.putBoolean(key, (Boolean) value);
        }
        else if (value instanceof Set)
        {
            //noinspection unchecked
            editor.putStringSet(key, (Set<String>) value);
        }
        else
        {
            putSuccess = false;
        }
        if (putSuccess)
        {
            editor.apply();
        }
        return putSuccess;
    }

    public static boolean isNeedToGuide(Context context)
    {
        return getValue(context, KEY_NEED_GUIDE, true);
    }

    public static void notifyGuideSuccess(Context context)
    {
        setValue(context, KEY_NEED_GUIDE, false);
    }

    public static void resetGuideState(Context context)
    {
        setValue(context, KEY_NEED_GUIDE, true);
    }

    public static int getVersionCode(Context context)
    {
        return getValue(context, KEY_APP_VERSION_CODE, 1);
    }

    public static String getVersionName(Context context)
    {
        return getValue(context, KEY_APP_VERSION_NAME, "");
    }

    public static void notifySaveVersion(Context context)
    {
        int versionCode = 1;
        String versionName = "1.0.0";
        try
        {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
            versionName = info.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        setValue(context, KEY_APP_VERSION_CODE, versionCode);
        setValue(context, KEY_APP_VERSION_NAME, versionName);
    }
}
