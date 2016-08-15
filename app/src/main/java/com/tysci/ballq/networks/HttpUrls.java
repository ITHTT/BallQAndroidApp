package com.tysci.ballq.networks;

import android.content.Context;
import android.text.TextUtils;

import com.tysci.ballq.activitys.BqServerCustomActivity;
import com.tysci.ballq.utils.SharedPreferencesUtil;

/**
 * Created by HTT
 * on 2016/3/2.
 */
@SuppressWarnings("ConstantConditions")
public class HttpUrls
{
    public static String CIRCLE_HOST_URL;
    public static String CIRCLE_HOST_URL_V1;
    public static String CIRCLE_HOST_URL_V2;

    public static String HOST_URL;

    //public static final String CIRCLE_HOST_URL_V1="http://apijt.ballq.cn/ballq/api/v1/";
    //public static final String HOST_URL="http://apit.ballq.cn";

    public static String HOST_URL_V1;
    public static String HOST_URL_V2;
    public static String HOST_URL_V3;
    public static String HOST_URL_V5;
    public static String HOST_URL_V6;

    public static String IMAGE_HOST_URL;

    public static String USER_PHONE_LOGIN_URL;
    public static String GET_WECHAT_TOKEN_URL;
    public static String GET_WECHAT_USER_IFNO_URL;
    public static String CHECK_USER_PHONE_URL;
    public static String GET_VCODE_URL;
    public static String RESET_USER_PASSWORD_URL;
    public static String USER_REGISTER_URL;
    public static String USER_WECHAT_LOGIN_URL;
    public static String TIP_OFF_LIST_URL;
    public static String BQ_INDINAN_ORDER_NeedUserToken;

    public static void initialized(Context context)
    {
        /**
         * 正式服标签
         */
        final boolean isFormatServerFlag = true;
        HOST_URL = "";
        CIRCLE_HOST_URL = "";

        long changeServerTimeMillis = SharedPreferencesUtil.getValue(context, BqServerCustomActivity.KEY_SERVER_CHANGE_TIME_MILLIS_FLAG, 0L);
        // 修改后的服务器保存一定时间，超过这个时间后清除
        if (System.currentTimeMillis() - changeServerTimeMillis >= 60 * 60 * 1000)
        {
            SharedPreferencesUtil.setValue(context, BqServerCustomActivity.KEY_SERVER, "");
            SharedPreferencesUtil.setValue(context, BqServerCustomActivity.KEY_CIRCLE_SERVER, "");
        }
        else
        {
            HOST_URL = SharedPreferencesUtil.getValue(context, BqServerCustomActivity.KEY_SERVER, "");
            CIRCLE_HOST_URL = SharedPreferencesUtil.getValue(context, BqServerCustomActivity.KEY_CIRCLE_SERVER, "");
        }
        if (TextUtils.isEmpty(CIRCLE_HOST_URL))
        {
            // 圈子服务主机地址
            if (isFormatServerFlag)
                CIRCLE_HOST_URL = "http://apijt.ballq.cn/";// 正式服
            else
                CIRCLE_HOST_URL = "http://int.ballq.cn:8003/";// 测试服
        }
        if (TextUtils.isEmpty(HOST_URL))
        {
            // 其他接口主机地址
            if (isFormatServerFlag)
                HOST_URL = "http://apit.ballq.cn";// 正式服
            else
                HOST_URL = "http://int.ballq.cn:8004";// 测试服
        }

        CIRCLE_HOST_URL_V1 = CIRCLE_HOST_URL + "ballq/api/v1/";
        CIRCLE_HOST_URL_V2 = CIRCLE_HOST_URL + "ballq/api/v2/";

        HOST_URL_V1 = HOST_URL + "/api/v1/";
        HOST_URL_V2 = HOST_URL + "/api/v2/";
        HOST_URL_V3 = HOST_URL + "/api/v3/";
        HOST_URL_V5 = HOST_URL + "/api/v5/";
        HOST_URL_V6 = HOST_URL + "/api/v6/";

        // 图片主机地址
        IMAGE_HOST_URL = "http://static-cdn.ballq.cn/";

        // 手机登录
        USER_PHONE_LOGIN_URL = HOST_URL_V1 + "token/new/";

        // 获取微信Token
        GET_WECHAT_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
        // 获取微信用户信息
        GET_WECHAT_USER_IFNO_URL = "https://api.weixin.qq.com/sns/userinfo";

        // 检测手机号是否已注册
        CHECK_USER_PHONE_URL = HOST_URL_V1 + "user/check_user_name/";
        // 获取验证码
        GET_VCODE_URL = HOST_URL_V1 + "user/verify_code_send/";
        // 重置密码
        RESET_USER_PASSWORD_URL = HOST_URL_V1 + "user/reset_password/";
        // 用户注册
        USER_REGISTER_URL = HOST_URL_V1 + "user/register_by_phone/";

        // 用户微信登录方式
        USER_WECHAT_LOGIN_URL = HOST_URL_V1 + "user/wechat_login/";

        // 爆料列表
        TIP_OFF_LIST_URL = HOST_URL + "/api/ares/tips/?settled=-1&etype=";

        // 球商夺宝地址，需要user和token
//         BQ_INDINAN_ORDER_NeedUserToken = "http://int.ballq.cn:8002/ballq/indiana/order";// 测试服
        BQ_INDINAN_ORDER_NeedUserToken = "http://adminj.ballq.cn/ballq/indiana/order";// 正式服
    }

    /**
     * 获取用户信息
     */
    public static String getUserInfoUrl(String userId)
    {
        return HOST_URL_V5 + "user/" + userId + "/profile/";
    }

    public static String getImageUrl(String url)
    {
        if (!url.contains("http://") && !url.contains("https://"))
        {
            return IMAGE_HOST_URL + url;
        }
        return url;
    }


}
