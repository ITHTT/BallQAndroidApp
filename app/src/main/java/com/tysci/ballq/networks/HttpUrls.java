package com.tysci.ballq.networks;

/**
 * Created by Administrator on 2016/3/2.
 */
public class HttpUrls
{
    public static final String CIRCLE_HOST_URL_V1;
    public static final String CIRCLE_HOST_URL_V2;

    public static final String HOST_URL;

    //public static final String CIRCLE_HOST_URL_V1="http://apijt.ballq.cn/ballq/api/v1/";
    //public static final String HOST_URL="http://apit.ballq.cn";

    public static final String HOST_URL_V1;
    public static final String HOST_URL_V2;
    public static final String HOST_URL_V3;
    public static final String HOST_URL_V5;
    public static final String HOST_URL_V6;

    /**
     * 图片主机地址
     */
    public static final String IMAGE_HOST_URL;
    //    /**
//     * 首页热门圈子列表URL
//     */
//    public static final String HOT_CIRCLE_LIST_URL = CIRCLE_HOST_URL_V1 + "bbs/topic/hots";
//    /**
//     * 球经列表URL
//     */
//    public static final String BALLQ_INFO_LIST_URL = HOST_URL_V5 + "articles/";
    public static final String USER_PHONE_LOGIN_URL;
    public static final String GET_WECHAT_TOKEN_URL;
    public static final String GET_WECHAT_USER_IFNO_URL;
    public static final String CHECK_USER_PHONE_URL;
    public static final String GET_VCODE_URL;
    public static final String RESET_USER_PASSWORD_URL;
    public static final String USER_REGISTER_URL;
    public static final String USER_WECHAT_LOGIN_URL;
    public static final String TIP_OFF_LIST_URL;
    public static final String BQ_INDINAN_ORDER_NeedUserToken;

    static
    {
        // 圈子服务主机地址
        CIRCLE_HOST_URL_V1 = "http://int.ballq.cn:8003/ballq/api/v1/";
        CIRCLE_HOST_URL_V2 = "http://int.ballq.cn:8003/ballq/api/v2/";

        // 其他接口主机地址
        HOST_URL = "http://int.ballq.cn:8004";
        HOST_URL_V1 = HOST_URL + "/api/v1/";
        HOST_URL_V2 = HOST_URL + "/api/v2/";
        HOST_URL_V3 = HOST_URL + "/api/v3/";
        HOST_URL_V5 = HOST_URL + "/api/v5/";
        HOST_URL_V6 = HOST_URL + "/api/v6/";

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
//        BQ_INDINAN_ORDER_NeedUserToken = "http://int.ballq.cn:8002/ballq/indiana/order";
        BQ_INDINAN_ORDER_NeedUserToken = "http://adminj.ballq.cn/ballq/indiana/order";
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
