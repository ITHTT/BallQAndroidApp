package com.tysci.ballq.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by LinDe on 2016/6/16.
 *
 * @see Toast
 */
public class ToastUtil {

    private static Toast mToast;

    static {
        mToast = null;
    }

    private ToastUtil() {
    }

//    public static void show(Context context, @StringRes int stringResID) {
//        show(context, stringResID, false);
//    }

//    public static void show(Context context, @StringRes int stringResID, boolean lengthLong) {
//        show(context, context.getResources().getString(stringResID), lengthLong);
//    }
//
//    public static void show(Context context, String message) {
//        show(context, message, false);
//    }

//    public static void show(Context context, String message, boolean lengthLong) {
//        if (mToast != null) {
//            mToast.cancel();
//        }
//        mToast = Toast.makeText(context, message, lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
//        mToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
//        mToast.show();
//    }

    public static <T> void show(Context context, T message) {
        show(context, message, false);
    }

    public static <T> void show(Context context, T message, boolean isShowLengthTime) {
        String msgResult;
        if (message == null) {
            msgResult = "";
        } else if (message instanceof Integer) {
            try {
                msgResult = context.getResources().getString((Integer) message);
            } catch (Resources.NotFoundException e) {
                msgResult = message.toString();
            }
        } else {
            msgResult = message.toString();
        }

        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(context, msgResult, isShowLengthTime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
        mToast.show();
    }
}
