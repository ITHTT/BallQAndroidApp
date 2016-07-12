package com.tysci.ballq.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tysci.ballq.R;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/5/31.
 */
public class WeChatUtil {
    public static final String APP_ID_WECHAT = "wx764a44d6325f4975";
    public static final String APP_SECRET_WECHAT = "35758ebd06710d202acf6270ce1be146";
    public static final String APP_MCH_ID_WECHAT = "1235168302";

    public static final String OPEN_ID="open_id";

    public static IWXAPI wxApi;

    public static void registerWXApi(Context context){
        wxApi= WXAPIFactory.createWXAPI(context,APP_ID_WECHAT, true);
        wxApi.registerApp(APP_ID_WECHAT);
    }

    /**
     * 微信登录
     * @param context
     * @return
     */
    public static boolean weChatLogin(Context context) {
        if(wxApi!=null) {
            boolean isInstalled = wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI();
            if (!isInstalled) {
                Toast.makeText(context, "请先安装微信APP",Toast.LENGTH_SHORT);
                return false;
            }
            // send oauth request
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo";
            wxApi.sendReq(req);
            return true;
        }
        return false;
    }

    /**
     * 获取微信Token
     * @param context
     * @param tag
     * @param code
     * @param loadingProgressDialog
     */
    public static void getWeChatToken(final Context context, final String tag, String code, final LoadingProgressDialog loadingProgressDialog){
        String url= HttpUrls.GET_WECHAT_TOKEN_URL+"?appid="+APP_ID_WECHAT
                +"&secret="+APP_SECRET_WECHAT
                +"&code="+code
                +"&grant_type=authorization_code";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(tag, url, 120 * 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }
            @Override
            public void onError(Call call, Exception error) {
                if(loadingProgressDialog!=null) {
                    loadingProgressDialog.dismiss();
                }
            }
            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        if(!TextUtils.isEmpty(obj.getString("access_token"))) {
                            getWeChatUserInfo(context,tag,obj,loadingProgressDialog);
                            return;
                        }
                    }
                }
                if(loadingProgressDialog!=null) {
                    loadingProgressDialog.dismiss();
                }

            }
            @Override
            public void onFinish(Call call) {

            }
        });
    }

    public static void getWeChatUserInfo(final Context context, String tag, JSONObject userInfoObj, final LoadingProgressDialog loadingProgressDialog){
        String token= userInfoObj.getString("access_token");
        String openId=userInfoObj.getString("openid");
        String url= HttpUrls.GET_WECHAT_USER_IFNO_URL+"?access_token="+token+"&openid="+openId;
        HttpClientUtil.getHttpClientUtil().sendGetRequest(tag, url, 120 * 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }
            @Override
            public void onError(Call call, Exception error) {
                if(loadingProgressDialog!=null) {
                    loadingProgressDialog.dismiss();
                }
            }
            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        if(!TextUtils.isEmpty(obj.getString("openid"))) {
                            WeChatUtil.setOpenId(context,obj.getString("openid"));
                            return;
                        }
                    }
                }
                if(loadingProgressDialog!=null) {
                    loadingProgressDialog.dismiss();
                }
            }
            @Override
            public void onFinish(Call call) {

            }
        });
    }

    public static void setOpenId(Context context,String openId){
        SharedPreferencesUtil.setStringValue(context,OPEN_ID,openId);
    }

    public static String getOpenId(Context context){
        return SharedPreferencesUtil.getStringValue(context,OPEN_ID);
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static HashMap<String,String> getWeChatPayParams(Context context,String etype,float moneys){
        KLog.e("moneys:"+moneys);
        KLog.e("type:"+etype);
        HashMap<String,String>params=new HashMap<>();
        params.put("appid", APP_ID_WECHAT);
        params.put("mch_id", APP_MCH_ID_WECHAT);
        params.put("nonce_str", buildTransaction(APP_MCH_ID_WECHAT));
        params.put("product_id", buildTransaction(APP_MCH_ID_WECHAT));
        params.put("body", "Wechat");
        params.put("total_fee", (int) (moneys * 100) + "");
        params.put("openid",getOpenId(context));
        params.put("bounty_type", etype /*"article" : "tip"*/);
        return params;
    }

    public static void weChatPay(JSONObject data){
        PayReq req = new PayReq();

        req.appId = data.getString("appid");
        req.partnerId = data.getString("partnerid");
        req.prepayId = data.getString("prepayid");
        req.packageValue = data.getString("package");
        req.nonceStr = data.getString("noncestr");
        req.timeStamp = data.getString("timestamp");
        req.sign = data.getString("paySign");
        wxApi.sendReq(req);
    }

    /**
     * 微信分享
     * @param context
     * @param title
     * @param excerpt
     * @param shareUrl
     * @param tag
     * @param logo
     * @return
     */
    public static boolean shareWebPage(Context context,int tag,String title, String excerpt, String shareUrl) {
        if(wxApi!=null) {
            boolean isInstalled = wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI();
            if (!isInstalled) {
                Toast.makeText(context, "请先安装微信APP", Toast.LENGTH_SHORT);
                return false;
            }
            WXWebpageObject webPage = new WXWebpageObject();
            webPage.webpageUrl = shareUrl;
            WXMediaMessage msg = new WXMediaMessage(webPage);
            if (title != null && !"".equals(title)) {
                if (title.length() < 100) msg.title = title;
                else msg.title = title.substring(0, 100);
            } else {
                msg.title = "球商";
            }
            if (excerpt != null && !"".equals(excerpt)) {
                if (excerpt.length() < 100) {
                    msg.description = excerpt;
                } else {
                    msg.description = excerpt.substring(0, 100);
                }
            } else {
                msg.description = "竞猜";
            }
            Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_ballq_logo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumb.compress(Bitmap.CompressFormat.PNG, 100, baos);
            msg.thumbData = baos.toByteArray();
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = tag;
            req.openId = getOpenId(context);
            wxApi.sendReq(req);
            return true;
        }
        return false;
    }
}
