package com.tysci.ballq.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by LinDe
 * on 2016-08-10 0010.
 */
public final class HandlerUtil
{
    private final Handler mHandler;

    public HandlerUtil() {mHandler = new Handler(Looper.getMainLooper());}

    public void post(Runnable runnable)
    {
        try
        {
            mHandler.post(runnable);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void postDelayed(Runnable runnable, long delayMillis)
    {
        try
        {
            mHandler.postDelayed(runnable, delayMillis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
