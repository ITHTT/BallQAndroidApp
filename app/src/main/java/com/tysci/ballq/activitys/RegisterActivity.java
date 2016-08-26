package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/1.
 */
public class RegisterActivity extends BaseActivity {
    @Bind(R.id.etPhoneNum)
    protected EditText etPhoneNum;
    @Bind(R.id.cbPhoneNumberRight)
    protected CheckBox checkPhoneNum;
    @Bind(R.id.etVCode)
    protected EditText etVCode;
    @Bind(R.id.tvGetVerify)
    protected TextView tvGetVCode;
    @Bind(R.id.etPassword)
    protected EditText etPassword;
    @Bind(R.id.cbShowPassword)
    protected CheckBox showPassword;
    @Bind(R.id.cbAgree)
    protected CheckBox cbAgree;
    @Bind(R.id.tv_commit)
    protected TextView tvCommit;
    @Bind(R.id.layout_agree_BallQRules)
    protected LinearLayout layoutAgreeBallQRules;

    private boolean isModifyPassword=false;

    private TimeCount timeCount=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews() {
        setTitle("手机注册");
        checkPhoneNum.setVisibility(View.INVISIBLE);
        tvGetVCode.setEnabled(false);
        showPassword.setChecked(false);
        tvCommit.setEnabled(false);
        setViewListener();
        timeCount=new TimeCount(60000,1000);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {
        if(intent!=null){
            isModifyPassword=intent.getBooleanExtra(Tag,false);
            if(isModifyPassword){
                setTitle("忘记密码");
                layoutAgreeBallQRules.setVisibility(View.GONE);
            }
        }
    }

    private void setViewListener(){
        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String text = s.toString();
                boolean isGood=!TextUtils.isEmpty(text)&&text.length()==11;
                checkPhoneNum.setVisibility(isGood ? View.VISIBLE : View.INVISIBLE);
                tvGetVCode.setEnabled(isGood);
                checkRegister();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etVCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRegister();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRegister();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etPassword.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : null);
                etPassword.setSelection(etPassword.getText().toString().length());
            }
        });
    }

    private void checkRegister(){
        String phone=etPhoneNum.getText().toString();
        String vcode=etVCode.getText().toString();
        String password=etPassword.getText().toString();
        boolean isRegister=!TextUtils.isEmpty(phone)&&phone.length()>=11
                           &&!TextUtils.isEmpty(vcode)&&vcode.length()>=6
                           &&!TextUtils.isEmpty(password)&&password.length()>=6;
        tvCommit.setEnabled(isRegister);
    }

    /**
     * 点击获取验证码
     * @param view
     */
    @OnClick(R.id.tvGetVerify)
    protected void onClickGetVCode(View view){
        String phone=etPhoneNum.getText().toString();
        if(TextUtils.isEmpty(phone)||phone.length()<6){
            return;
        }
        etVCode.requestFocus();
        if(isModifyPassword){
            getVCode(phone);
        }else{
            checkPhone(phone);
        }
    }

    /**
     * 获取验证码
     */
    private void getVCode(String phone){
        Map<String,String>params=new HashMap<>(1);
        params.put("phone_number", phone);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, HttpUrls.GET_VCODE_URL, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
                etPhoneNum.setEnabled(false);
                tvGetVCode.setEnabled(false);
                tvGetVCode.setText("请求中...");

            }

            @Override
            public void onError(Call call, Exception error) {
                etPhoneNum.setEnabled(true);
                tvGetVCode.setEnabled(true);
                tvGetVCode.setText("获取验证码");
            }

            @Override
            public void onSuccess(Call call, String response) {
                timeCount.start();
            }

            @Override
            public void onFinish(Call call) {

            }
        });

    }

    /**
     * 检查手机号
     */
    private void checkPhone(final String phone){
        Map<String,String>params=new HashMap<>(2);
        params.put("username",phone);
        params.put("check_type","phone_number");
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, HttpUrls.CHECK_USER_PHONE_URL, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
                etPhoneNum.setEnabled(false);
                tvGetVCode.setEnabled(false);
                tvGetVCode.setText("请求中...");
            }

            @Override
            public void onError(Call call, Exception error) {
                etPhoneNum.setEnabled(true);
                tvGetVCode.setEnabled(true);
                tvGetVCode.setText("获取验证码");
            }

            @Override
            public void onSuccess(Call call, String response) {
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        int status = obj.getIntValue("status");
                        String msg = obj.getString("message");
                        if (status == 0 && !TextUtils.isEmpty(msg) && msg.equalsIgnoreCase("ok")) {
                            getVCode(phone);
                            return;
                        }
                    }
                }
                etPhoneNum.setEnabled(true);
                tvGetVCode.setEnabled(true);
                tvGetVCode.setText("获取验证码");
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    /**
     * 重置密码
     */
    private void resetUserPassword(){
        final String phone=etPhoneNum.getText().toString();
        String vcode=etVCode.getText().toString();
        final String password=etPassword.getText().toString();
        if(TextUtils.isEmpty(phone)||phone.length()<11){
            return;
        }

        if(TextUtils.isEmpty(vcode)||vcode.length()<6){
            return;
        }

        if(TextUtils.isEmpty(password)||password.length()<6){
            return;
        }

        Map<String,String>params=new HashMap<String,String>(3);
        params.put("username",phone);
        params.put("new_password",password);
        params.put("verify_code",vcode);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, HttpUrls.RESET_USER_PASSWORD_URL, params, new HttpClientUtil.StringResponseCallBack() {
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
                        int status=obj.getIntValue("status");
                        if(status==307){
                            Intent intent=getIntent();
                            intent.putExtra("phone",phone);
                            intent.putExtra("password",password);
                            setResult(RESULT_OK,intent);
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

    /**
     * 用户注册
     */
    private void register(){
        final String phone=etPhoneNum.getText().toString();
        String vcode=etVCode.getText().toString();
        final String password=etPassword.getText().toString();
        if(TextUtils.isEmpty(phone)||phone.length()<11){
            return;
        }

        if(TextUtils.isEmpty(vcode)||vcode.length()<6){
            return;
        }

        if(TextUtils.isEmpty(password)||password.length()<6){
            return;
        }

        Map<String,String>params=new HashMap<String,String>(3);
        params.put("phone_number",phone);
        params.put("password",password);
        params.put("verify_code",vcode);
        params.put("nickname","BallQ"+System.currentTimeMillis());
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, HttpUrls.USER_REGISTER_URL, params, new HttpClientUtil.StringResponseCallBack() {
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
                        int status=obj.getIntValue("status");
                        if(status==307){
                            Intent intent=getIntent();
                            intent.putExtra("phone",phone);
                            intent.putExtra("password", password);
                            setResult(RESULT_OK, intent);
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

    @OnClick(R.id.tv_commit)
    protected void userRegister(View view){
        if(isModifyPassword){
            resetUserPassword();
        }else{
            if(!cbAgree.isChecked()){
                return;
            }
            register();
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
    protected void userLogin(UserInfoEntity userInfoEntity) {

    }

    @Override
    protected void userExit() {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timeCount!=null) {
            timeCount.cancel();
        }
    }

    public class TimeCount extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetVCode.setText("获取验证码("+millisUntilFinished/1000+")");
        }

        @Override
        public void onFinish() {
            etPhoneNum.setEnabled(true);
            tvGetVCode.setEnabled(true);
            tvGetVCode.setText("获取验证码");
        }
    }
}
