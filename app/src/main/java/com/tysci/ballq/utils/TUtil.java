package com.tysci.ballq.utils;

import android.content.res.Resources;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-07-15 0015.
 */
public final class TUtil {
    private static TUtil instance;

    private TUtil() {
    }

    public static TUtil getInstance() {
        if (instance == null) {
            synchronized (TUtil.class) {
                if (instance == null) {
                    instance = new TUtil();
                }
            }
        }
        return instance;
    }

    public <T> TUtil setText(TextView tv, T t) {
        String result;
        if (t == null) {
            result = "";
        } else if (t instanceof Integer) {
            try {
                result = tv.getContext().getResources().getString((Integer) t);
            } catch (Resources.NotFoundException e) {
                result = t.toString();
            }
        } else {
            result = t.toString();
        }
        tv.setText(result);
        return this;
    }
}
