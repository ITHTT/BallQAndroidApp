package com.tysci.ballq.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.utils.WeChatUtil;
import com.tysci.ballq.views.adapters.UserRewardMoneyAdapter;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.dialogs.UserRewardPayWayDialog;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler,AdapterView.OnItemClickListener,UserRewardPayWayDialog.OnUserRewardListener
{
    @Bind(R.id.ivUserIcon)
    protected CircleImageView ivUserIcon;
    @Bind(R.id.isV)
    protected ImageView iV;
    @Bind(R.id.gridView)
    protected GridView gvRewards;

    private String pt;
    private int isV;
    private String uid;
    private int id;
    private String type;
    private float moneys=0f;

    private List<String> rewardMoneys;
    private UserRewardMoneyAdapter adapter=null;
    private UserRewardPayWayDialog rewardPayWayDialog=null;
    private LoadingProgressDialog loadingProgressDialog=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_reward;
    }

    @Override
    protected void initViews() {
        setTitle("用户打赏");
        gvRewards.setOnItemClickListener(this);
        rewardMoneys= Arrays.asList(this.getResources().getStringArray(R.array.reward_moneys));
        adapter=new UserRewardMoneyAdapter(rewardMoneys);
        gvRewards.setAdapter(adapter);
    }

    private void showProgressDialog(String msg){
        if(loadingProgressDialog==null){
            loadingProgressDialog=new LoadingProgressDialog(this);
            loadingProgressDialog.setCanceledOnTouchOutside(false);
        }
        loadingProgressDialog.setMessage(msg);
        loadingProgressDialog.show();
    }

    private void dimssProgressDialog(){
        if(loadingProgressDialog!=null&&loadingProgressDialog.isShowing()){
            loadingProgressDialog.dismiss();
        }
    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {
        pt=intent.getStringExtra("pt");
        isV=intent.getIntExtra("is_v",0);
        uid=intent.getStringExtra("uid");
        id=intent.getIntExtra("id",-1);
        KLog.e("id:"+id);
        type=intent.getStringExtra("type");
        GlideImageLoader.loadImage(this,pt, R.mipmap.icon_user_default,ivUserIcon);
        UserInfoUtil.setUserHeaderVMark(isV,iV,ivUserIcon);
    }

    public static void userReward(Context context, String type, String uid, int id, String pt, int isV){
        if(UserInfoUtil.checkLogin(context)) {
            Intent intent = new Intent(context, WXPayEntryActivity.class);
            intent.putExtra("uid", uid);
            intent.putExtra("id", id);
            intent.putExtra("pt", pt);
            intent.putExtra("is_v", isV);
            intent.putExtra("type", type);
            context.startActivity(intent);
        }else{
            UserInfoUtil.userLogin(context);
        }
    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle outState) {

    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {
        if("user_reward".equals(action)){
           String code= data.getString("code");
            KLog.e("发送通知接收到的数据:"+code);
            if(!TextUtils.isEmpty(code)){
                getWeChatToken(code,moneys);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        if(WeChatUtil.wxApi != null) {
            KLog.e("注册回调方法...");
            WeChatUtil.wxApi.handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq arg0)
    {

    }

    @Override
    public void onResp(final BaseResp resp)
    {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            switch (resp.errCode)
            {
                case 0:
                    KLog.e("打赏成功");
                    break;
                case -1:
                    KLog.e("打赏失败");
                    break;
                case -2:
                    KLog.e("打赏取消");
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(rewardPayWayDialog==null){
            rewardPayWayDialog=new UserRewardPayWayDialog(this);
            rewardPayWayDialog.setTag(Tag);
            rewardPayWayDialog.setOnUserRewardListener(this);
        }
        rewardPayWayDialog.setRewardMoneys(Float.parseFloat(rewardMoneys.get(position)));
        rewardPayWayDialog.show();
    }

    @Override
    public void onWeChatPayWay(float rewardMoneys) {
        moneys=rewardMoneys;
        if(TextUtils.isEmpty(WeChatUtil.getOpenId(this))){
            /**进行微信授权登录*/
            boolean isSuccess= WeChatUtil.weChatLogin(this);
            if(isSuccess){
                WXEntryActivity.REQUEST_TAG=2;
                showProgressDialog("微信授权中...");
            }
        }else {
            weChatPay(rewardMoneys);
        }
    }

    @Override
    public void onUserBalancePayWay(float rewardMoneys) {

    }

    public void getWeChatToken(String code,final float moneys){
        String url= HttpUrls.GET_WECHAT_TOKEN_URL+"?appid="+ WeChatUtil.APP_ID_WECHAT
                +"&secret="+ WeChatUtil.APP_SECRET_WECHAT
                +"&code="+code
                +"&grant_type=authorization_code";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 120 * 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }
            @Override
            public void onError(Call call, Exception error) {
                dimssProgressDialog();
            }
            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        if(!TextUtils.isEmpty(obj.getString("access_token"))) {
                            getWeChatUserInfo(obj,moneys);
                            return;
                        }
                    }
                }
                dimssProgressDialog();
            }
            @Override
            public void onFinish(Call call) {

            }
        });
    }

    public  void getWeChatUserInfo(JSONObject userInfoObj,final float moneys){
        String token= userInfoObj.getString("access_token");
        String openId=userInfoObj.getString("openid");
        String url= HttpUrls.GET_WECHAT_USER_IFNO_URL+"?access_token="+token+"&openid="+openId;
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 120 * 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }
            @Override
            public void onError(Call call, Exception error) {
                dimssProgressDialog();
            }
            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        if(!TextUtils.isEmpty(obj.getString("openid"))) {
                            WeChatUtil.setOpenId(WXPayEntryActivity.this,obj.getString("openid"));
                            weChatPay(moneys);
                            return;
                        }
                    }
                }
                dimssProgressDialog();
            }
            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void weChatPay(float moneys){
        String url= HttpUrls.HOST_URL_V5 + type + "/" + id + "/new_bounty/";
        HashMap<String,String> params= WeChatUtil.getWeChatPayParams(this,type,moneys);
        if(UserInfoUtil.checkLogin(this)){
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        JSONObject dataObj=obj.getJSONObject("data");
                        if(dataObj!=null&&!dataObj.isEmpty()){
                            WeChatUtil.weChatPay(dataObj);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WXEntryActivity.REQUEST_TAG=0;
    }
}
