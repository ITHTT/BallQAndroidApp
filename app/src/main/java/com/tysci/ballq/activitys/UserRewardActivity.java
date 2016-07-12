package com.tysci.ballq.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
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

/**
 * Created by Administrator on 2016/6/20.
 */
public class UserRewardActivity extends BaseActivity implements AdapterView.OnItemClickListener,UserRewardPayWayDialog.OnUserRewardListener{
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

    private List<String> rewardMoneys;
    private UserRewardMoneyAdapter adapter=null;
    private UserRewardPayWayDialog rewardPayWayDialog=null;
    private LoadingProgressDialog progressDialog=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_reward;
    }

    @Override
    protected void initViews() {
        setTitle("用户打赏");
        gvRewards.setOnItemClickListener(this);
        rewardMoneys=Arrays.asList(this.getResources().getStringArray(R.array.reward_moneys));
        adapter=new UserRewardMoneyAdapter(rewardMoneys);
        gvRewards.setAdapter(adapter);
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

    public static void userReward(Context context,String type,String uid,int id,String pt,int isV){
        if(UserInfoUtil.checkLogin(context)) {
            Intent intent = new Intent(context, UserRewardActivity.class);
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
        if(UserInfoUtil.getWechatUserInfo(this)==null){
            /**进行微信授权登录*/
            WeChatUtil.weChatLogin(this);
        }else {
            weChatPay(rewardMoneys);
        }
    }

    @Override
    public void onUserBalancePayWay(float rewardMoneys) {

    }

    private void weChatPay(float moneys){
        String url= HttpUrls.HOST_URL_V5 + type + "/" + id + "/new_bounty/";
        HashMap<String,String>params= WeChatUtil.getWeChatPayParams(this,type,moneys);
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
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }
}
