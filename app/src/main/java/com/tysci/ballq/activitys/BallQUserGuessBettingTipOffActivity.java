package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.fragments.BallQMatchListFragment;
import com.tysci.ballq.fragments.BallQMatchTipOffListFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQMatchGuessBettingEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.widgets.expandablelayout.ExpandableLinearLayout;
import com.tysci.ballq.views.widgets.togglebutton.ToggleButton;

import java.util.HashMap;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/30.
 */
public class BallQUserGuessBettingTipOffActivity extends BaseActivity {
    @Bind(R.id.tv_match_betting_info)
    protected TextView tvMatchBettingInfo;
    @Bind(R.id.tv_match_betting_moneys)
    protected TextView tvBettingMoneys;
    @Bind(R.id.layout_expandable_tip_off_info)
    protected ExpandableLinearLayout expandableLinearLayout;
    @Bind(R.id.et_betting_tip)
    protected EditText etBettingTip;
    @Bind(R.id.cb_faith_mark)
    protected CheckBox cbFaithMark;
    @Bind(R.id.rating_bar)
    protected RatingBar ratingBar;
    @Bind(R.id.slide_switch)
    protected ToggleButton toggleButton;

    private BallQMatchEntity matchEntity;
    private BallQMatchGuessBettingEntity bettingEntity;
    private boolean isTip=false;

    private LoadingProgressDialog loadingProgressDialog;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_betting_tip_edit;
    }

    @Override
    protected void initViews() {
        setTitle("爆料竞猜");
        setTitleRightAttributes();
        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                expandableLinearLayout.toggle();
            }
        });

        cbFaithMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ratingBar.setVisibility(View.VISIBLE);
                }else{
                    ratingBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {
        bettingEntity=intent.getParcelableExtra("betting_data");
        matchEntity=intent.getParcelableExtra("match_data");
        isTip=intent.getBooleanExtra("is_tip", false);
        toggleButton.setEnabled(!isTip);
        if(bettingEntity!=null){
            tvMatchBettingInfo.setText(bettingEntity.getBettingInfo());
            tvBettingMoneys.setText(String.valueOf(bettingEntity.getBettingMoney()));
        }
    }

    public void setTitleRightAttributes(){
        TextView btnRight=titleBar.getRightMenuTextView();
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText("发布");
        btnRight.setBackgroundResource(R.drawable.bt_ok_select_bg);
        btnRight.setGravity(Gravity.CENTER);
        btnRight.setWidth(CommonUtils.dip2px(this, 60));
        btnRight.setHeight(CommonUtils.dip2px(this, 30));
        btnRight.setTextColor(this.getResources().getColor(R.color.gold));
        btnRight.setTextSize(14);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestBetting();
                requestBetting();
            }
        });
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
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState) {

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

    private void requestBetting(){
        String cont=etBettingTip.getText().toString();
        String ratingValue=String.valueOf((int) (ratingBar.getRating() * 10));
        if(isTip){
            if(TextUtils.isEmpty(cont)||cont.length()<20){
                ToastUtil.show(this,"您必须要输入至少20个字符的投注理由");
                return;
            }
        }


        String url= HttpUrls.HOST_URL_V5 + "match/" + matchEntity.getEid() + "/add_tip/";
        final HashMap<String,String> params=new HashMap<>();
        params.put("matchId",String.valueOf(matchEntity.getEid()));
        params.put("stake",String.valueOf(bettingEntity.getBettingMoney()*100));
        params.put("choice",bettingEntity.getBettingType());
        params.put("otype",bettingEntity.getOtype());
        params.put("eid", String.valueOf(matchEntity.getEid()));
        params.put("etype",String.valueOf(matchEntity.getEtype()));
        params.put("oid",String.valueOf(bettingEntity.getId()));
        params.put("cont",cont);
        if(cbFaithMark.isChecked()){
            params.put("confidence",String.valueOf((int) (ratingBar.getRating()) * 10));
        }
        if (UserInfoUtil.checkLogin(this)) {
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        showProgressDialog("投注请求中...");
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                dimssProgressDialog();
                ToastUtil.show(BallQUserGuessBettingTipOffActivity.this, "请求失败");

            }

            @Override
            public void onSuccess(Call call, String response) {
                dimssProgressDialog();
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        ToastUtil.show(BallQUserGuessBettingTipOffActivity.this, obj.getString("message"));
                        int status=obj.getIntValue("status");
                        if(status==312||status==317){
                            setResult(RESULT_OK);
                            publishBettingSuccessEvent();
                            finish();
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

    private void publishBettingSuccessEvent(){
        EventObject eventObject=new EventObject();
        eventObject.addReceiver(BallQMatchListFragment.class, BallQMatchTipOffListFragment.class,BallQMatchGuessBettingActivity.class);
        eventObject.getData().putInt("eid",matchEntity.getEid());
        eventObject.getData().putInt("etype",matchEntity.getEtype());
        EventObject.postEventObject(eventObject, "betting_success");
    }

}
