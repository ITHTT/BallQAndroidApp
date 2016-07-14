package com.tysci.ballq.bigdata;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;

import com.tysci.ballq.R;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.utils.FileUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.RandomUtils;
import com.tysci.ballq.utils.SharedPreferencesUtil;
import com.tysci.ballq.utils.UserInfoUtil;

import java.io.File;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-14 0014.
 * 大数据采集
 */
public final class BigDataUtil {
    private static String CID, CCID, UUID;
    private static long inTimeMillis;
    private static String lastPosition;

    private BigDataUtil() {
    }

    public static void init(Context context) {
        Resources res = context.getResources();
        CID = res.getString(R.string.cid);
        CCID = res.getString(R.string.ccid);

        UUID = BigDataUtil.class.getSimpleName() + "/UUID";
    }

    public static void uploadNormal(Context c, BigDataST st, BigDataOPT opt, String position) {
        final String zero = String.valueOf(0);
        upload(c, st, opt, position, zero, zero, zero, zero, zero);
    }

    public static void uploadMatch(Context c, BigDataST st, BigDataOPT opt, String position, String matchId) {
        final String zero = String.valueOf(0);
        upload(c, st, opt, position, matchId, zero, zero, zero, zero);
    }

    public static void uploadTip(Context c, BigDataST st, BigDataOPT opt, String position, String tipId) {
        final String zero = String.valueOf(0);
        upload(c, st, opt, position, zero, tipId, zero, zero, zero);
    }

    public static void uploadArticle(Context c, BigDataST st, BigDataOPT opt, String position, String articleId) {
        final String zero = String.valueOf(0);
        upload(c, st, opt, position, zero, zero, articleId, zero, zero);
    }

    public static void uploadSfid(Context c, BigDataST st, BigDataOPT opt, String position, String sfid) {
        final String zero = String.valueOf(0);
        upload(c, st, opt, position, zero, zero, zero, sfid, zero);
    }

    public static void uploadStid(Context c, BigDataST st, BigDataOPT opt, String position, String stid) {
        final String zero = String.valueOf(0);
        upload(c, st, opt, position, zero, zero, zero, zero, stid);
    }

    private static void upload(Context c, BigDataST st, BigDataOPT opt, String position, String matchId, String tipid, String articleId, String sfid, String stid) {
        HttpClientUtil.getHttpClientUtil().sendGetRequest("BigData", getBigDataUrl(c, st, opt, position, matchId, tipid, articleId, sfid, stid), 0, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
            }

            @Override
            public void onError(Call call, Exception error) {
                KLog.e("大数据采集失败...");
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.d("大数据采集成功...");
            }

            @Override
            public void onFinish(Call call) {
            }
        });
    }

    private static String getBigDataUrl(Context c, BigDataST st, BigDataOPT opt, String position, String matchId, String tipid, String articleId, String sfid, String stid) {
        if (!isPositionFormatSuccess(position)) {
            throw new RuntimeException("BigData");
        }

        long rt = 0;// 页面停留毫秒值
        if (opt == BigDataOPT.IN) {
            inTimeMillis = System.currentTimeMillis();
        }
        if (opt == BigDataOPT.OUT) {
            rt = System.currentTimeMillis() - inTimeMillis;
        }

        final String netString;
        switch (ConnectivityUtil.getNetworkType(c)) {
            case ConnectivityUtil.NONE:
                netString = "none";
                break;
            case ConnectivityUtil.WIFI:
                netString = "wifi";
                break;
            case ConnectivityUtil._2G:
                netString = "2g";
                break;
            case ConnectivityUtil._3G:
                netString = "3g";
                break;
            case ConnectivityUtil._4G:
                netString = "4g";
                break;
            case ConnectivityUtil.UNKNOWN:
            default:
                netString = "unknown";
                break;
        }

        final String url = "http://log.ballq.cn:8010/v1.html?sec=bd.tysci.com"
                + "&st=" + st.value// 运动类型
                + "&p=" + position // 当前位置（埋点编号ID）
                + "&src=" + lastPosition// 上级位置（埋点编号ID）
                + "&lang=" + Locale.getDefault() // 操作系统语言
                + "&sys=" + String.valueOf(0) // 操作系统类型 0 Android 1 IOS
                + "&ph=" + android.os.Build.BRAND  // 手机品牌
                + "&phv=" + android.os.Build.MODEL // 手机型号
                + "&uid=" + (UserInfoUtil.checkLogin(c) ? UserInfoUtil.getUserId(c) : "0")// 用户ID
                + "&imei=" + new PhoneInfo(c).getIMEI() // 手机码
                + "&appv=" + SharedPreferencesUtil.getVersionName(c) // APP版本号
                + "&net=" + netString // 网络类型
                + "&jid=" + JPushInterface.getRegistrationID(c) // 极光推送ID
                + "&opt=" + opt.value // 操作类型
                + "&rt=" + rt // 页面停留时间
                + "&mid=" + matchId
                + "&tipid=" + tipid
                + "&aid=" + articleId
                + "&sfid=" + sfid
                + "&stid=" + stid
                + "&cid=" + CID // 渠道ID
                + "&ccid=" + CCID//子渠道ID
                + "&uuid=" + getUuid(c);
        lastPosition = position;
        return url;
    }

    private static String getUuid(Context c) {
        final String path = Environment.getExternalStorageDirectory().getPath()
                + "/.BallQ/.bigData.txt";
        String uuid = SharedPreferencesUtil.getValue(c, UUID, "null");
        if (uuid.equals("null")) {
            String temp = FileUtil.readStringFromFile(new File(path));
            if (TextUtils.isEmpty(temp)) {
                uuid = RandomUtils.getRandomNumber(16);
            } else {
                uuid = temp;
            }
            SharedPreferencesUtil.setValue(c, UUID, uuid);
        }
        FileUtil.writeStringToFile(new File(path), uuid, false);
        if (TextUtils.isEmpty(uuid)) {
            uuid = RandomUtils.getRandomNumber(16);
            FileUtil.writeStringToFile(new File(path), uuid, false);
        }
        return uuid;
    }

    private static boolean isPositionFormatSuccess(String position) {
        try {
            final String[] tmpStrings = position.split(":");
            for (String tmp : tmpStrings) {
                //noinspection ResultOfMethodCallIgnored
                Integer.parseInt(tmp);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
