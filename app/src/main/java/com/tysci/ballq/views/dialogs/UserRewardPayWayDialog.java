package com.tysci.ballq.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.LoadDataLayout;

import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/6/20.
 */
public class UserRewardPayWayDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private LoadDataLayout loadDataLayout;
    private TextView tvUserMoneys;
    private String Tag=null;
    private LinearLayout layoutUserPayWays=null;
    private float userMoneys=0f;
    private OnUserRewardListener  onUserRewardListener;
    private float rewardMoneys=0f;

    private LoadingProgressDialog loadingProgressDialog=null;

    public UserRewardPayWayDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        initViews(context);
    }

    private void initViews(Context context){
        this.context=context;
        setContentView(R.layout.dialog_reward_pay_way);
        loadDataLayout= (LoadDataLayout) this.findViewById(R.id.layout_loading_data);
        layoutUserPayWays=(LinearLayout)this.findViewById(R.id.user_pay_way_info);
        tvUserMoneys=(TextView)this.findViewById(R.id.tvBalance);
        loadDataLayout.setLoadingTextColor(Color.parseColor("#ffffff"));
        this.findViewById(R.id.layoutPayWeChat).setOnClickListener(this);
        this.findViewById(R.id.layoutPayBalance).setOnClickListener(this);
        this.findViewById(R.id.tvCancel).setOnClickListener(this);
    }

    public void setTag(String tag){
        this.Tag=tag;
    }

    public void setOnUserRewardListener(OnUserRewardListener listener){
        this.onUserRewardListener=listener;
    }

    public void setRewardMoneys(float moneys){
        this.rewardMoneys=moneys;
    }

    private void getUserAccountInfo(){
        String url= HttpUrls.HOST_URL_V5 + "my_account/";
        HashMap<String,String> params=null;
        if(UserInfoUtil.checkLogin(context)){
            params=new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(context));
            params.put("token", UserInfoUtil.getUserToken(context));
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
                layoutUserPayWays.setVisibility(View.VISIBLE);
                loadDataLayout.hideLoad();
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        JSONArray dataArrays=obj.getJSONArray("data");
                        if(dataArrays!=null&&!dataArrays.isEmpty()){
                            JSONObject dataObj=dataArrays.getJSONObject(0);
                            if(dataObj!=null&&!dataObj.isEmpty()){
                                userMoneys=dataObj.getFloat("rmb");
                                tvUserMoneys.setText("零钱支付("+String.format(Locale.getDefault(),"%.2f",userMoneys/100f)+")");
                            }
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
    public void show() {
        loadDataLayout.showLoading();
        layoutUserPayWays.setVisibility(View.GONE);
        getUserAccountInfo();
        super.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogWindowAttrs();
    }

    protected void setDialogWindowAttrs() {
        // TODO Auto-generated method stub
        Activity activity = (Activity) context;
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        Window window=this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams p = window.getAttributes();
        //p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.4
        p.width = (int) (d.getWidth());
        window.setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.layoutPayWeChat:
                if(onUserRewardListener!=null){
                    onUserRewardListener.onWeChatPayWay(rewardMoneys);
                }
                break;
            case R.id.layoutPayBalance:
                if(rewardMoneys>userMoneys){
                    ToastUtil.show(getContext(),"抱歉，您余额不足");
                    return;
                }
                if(onUserRewardListener!=null){
                    onUserRewardListener.onUserBalancePayWay(rewardMoneys);
                }
                break;
            case R.id.tvCancel:
                dismiss();
                break;
        }
    }

    public interface OnUserRewardListener{
        void onWeChatPayWay(float rewardMoneys);
        void onUserBalancePayWay(float rewardMoneys);
    }
}
