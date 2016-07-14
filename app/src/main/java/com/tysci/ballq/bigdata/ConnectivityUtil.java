package com.tysci.ballq.bigdata;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by LinDe on 2016-07-14 0014.
 * ConnectivityUtil
 */
public class ConnectivityUtil {
    private ConnectivityUtil() {
    }

    public static byte getNetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null) return NONE;
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                return WIFI;
            case ConnectivityManager.TYPE_MOBILE:
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_CDMA:// 电信2G
                    case TelephonyManager.NETWORK_TYPE_GPRS:// 联通2g
                    case TelephonyManager.NETWORK_TYPE_EDGE:// 移动2G
                        return _2G;
                    case TelephonyManager.NETWORK_TYPE_UMTS:// 联通3g
                    case TelephonyManager.NETWORK_TYPE_HSDPA:// 联通3g
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:// 电信3g
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:// 电信3g
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:// 电信3g
                        return _3G;
                    case TelephonyManager.NETWORK_TYPE_LTE:// 准4g（3g到4g的一个过度）
                        return _4G;
                    case TelephonyManager.NETWORK_TYPE_1xRTT:// 1xRTT
                    case TelephonyManager.NETWORK_TYPE_EHRPD:// eHRPD
                    case TelephonyManager.NETWORK_TYPE_HSPA:// HSPA
                    case TelephonyManager.NETWORK_TYPE_HSPAP:// HSPA+
                    case TelephonyManager.NETWORK_TYPE_HSUPA:// HSUPA
                    case TelephonyManager.NETWORK_TYPE_IDEN:// iDen
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN://
                        return UNKNOWN;
                }
            default:
                return UNKNOWN;
        }
    }

    public static final byte NONE = 0x0;
    public static final byte WIFI = 0x1;
    public static final byte _2G = 0x2;
    public static final byte _3G = 0x3;
    public static final byte _4G = 0x4;
    public static final byte UNKNOWN = 0x5;
}
