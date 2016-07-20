package com.tysci.ballq.networks;

/**
 * Created by Administrator on 2016/3/2.
 */
public class HttpUrls {
    /**
     * 圈子服务主机地址
     */
    public static final String CIRCLE_HOST_URL = "http://int.ballq.cn:8003/ballq/api/v1/";

    /**
     * 其他接口主机地址
     */
    public static final String HOST_URL = "http://int.ballq.cn:8004";

    //public static final String CIRCLE_HOST_URL="http://apijt.ballq.cn/ballq/api/v1/";
    //public static final String HOST_URL="http://apit.ballq.cn";

    public static final String HOST_URL_V1 = HOST_URL + "/api/v1/";
    public static final String HOST_URL_V2 = HOST_URL + "/api/v2/";
    public static final String HOST_URL_V3 = HOST_URL + "/api/v3/";
    public static final String HOST_URL_V5 = HOST_URL + "/api/v5/";
    public static final String HOST_URL_V6 = HOST_URL + "/api/v6/";

    /**
     * 图片主机地址
     */
    public static final String IMAGE_HOST_URL = "http://static-cdn.ballq.cn/";
    /**
     * 首页热门圈子列表URL
     */
    public static final String HOT_CIRCLE_LIST_URL = CIRCLE_HOST_URL + "bbs/topic/hots";
    /**
     * 球经列表URL
     */
    public static final String BALLQ_INFO_LIST_URL = HOST_URL_V5 + "articles/";

    /**手机登录*/
    public static final String USER_PHONE_LOGIN_URL=HOST_URL_V1+"token/new/";
    /**获取微信Token*/
    public static final String GET_WECHAT_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token";
    /**获取微信用户信息*/
    public static final String GET_WECHAT_USER_IFNO_URL="https://api.weixin.qq.com/sns/userinfo";
    /**检测手机号是否已注册*/
    public static final String CHECK_USER_PHONE_URL=HOST_URL_V1+"user/check_user_name/";
    /**获取验证码*/
    public static final String GET_VCODE_URL=HOST_URL_V1+"user/verify_code_send/";
    /**重置密码*/
    public static final String RESET_USER_PASSWORD_URL=HOST_URL_V1+"user/reset_password/";
    /**用户注册*/
    public static final String USER_REGISTER_URL=HOST_URL_V1+"user/register_by_phone/";
    /**用户微信登录方式*/
    public static final String USER_WECHAT_LOGIN_URL=HOST_URL_V1+"user/wechat_login/";
    /**爆料列表*/
    public static final String TIP_OFF_LIST_URL=HOST_URL+"/api/ares/tips/?settled=-1&etype=";

    /**获取用户信息*/
    public static final String getUserInfoUrl(String userId){
        return HOST_URL_V5+"user/"+userId+"/profile/";
    }

    public static String getImageUrl(String url){
        if(!url.contains("http://")&&!url.contains("https://")){
            return IMAGE_HOST_URL+url;
        }
        return url;
    }




}
