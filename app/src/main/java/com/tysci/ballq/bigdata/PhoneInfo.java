package com.tysci.ballq.bigdata;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2016-07-14 0014.
 */
public class PhoneInfo {
    private final TelephonyManager telephonyManager;
    private final Context ctx;

    private String IMSI;

    /**
     * 获取手机国际识别码IMEI
     */
    public PhoneInfo(Context context) {
        ctx = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取手机号码
     */
    public String getNativePhoneNumber() {

        String nativePhoneNumber;
        nativePhoneNumber = telephonyManager.getLine1Number();

        return nativePhoneNumber;
    }

    /**
     * 获取手机服务商信息
     */
    public String getProvidersName() {
        String providerName = null;
        try {
            IMSI = telephonyManager.getSubscriberId();
            //IMSI前面三位460是国家号码，其次的两位是运营商代号，00、02是中国移动，01是联通，03是电信。
            System.out.print("IMSI是：" + IMSI);
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                providerName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                providerName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                providerName = "中国电信";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return providerName;

    }

    public String getIMEI() {
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取手机信息
     */
    public String getPhoneInfo() {

        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        return ("\nDeviceID(IMEI)" + tm.getDeviceId()) +
                "\nDeviceSoftwareVersion:" + tm.getDeviceSoftwareVersion() +
                "\ngetLine1Number:" + tm.getLine1Number() +
                "\nNetworkCountryIso:" + tm.getNetworkCountryIso() +
                "\nNetworkOperator:" + tm.getNetworkOperator() +
                "\nNetworkOperatorName:" + tm.getNetworkOperatorName() +
                "\nNetworkType:" + tm.getNetworkType() +
                "\nPhoneType:" + tm.getPhoneType() +
                "\nSimCountryIso:" + tm.getSimCountryIso() +
                "\nSimOperator:" + tm.getSimOperator() +
                "\nSimOperatorName:" + tm.getSimOperatorName() +
                "\nSimSerialNumber:" + tm.getSimSerialNumber() +
                "\ngetSimState:" + tm.getSimState() +
                "\nSubscriberId:" + tm.getSubscriberId() +
                "\nVoiceMailNumber:" + tm.getVoiceMailNumber();
    }
}
