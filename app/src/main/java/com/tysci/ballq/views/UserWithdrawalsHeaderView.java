package com.tysci.ballq.views;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.UserWithdrawsActivity;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.SoftInputUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.utils.WeChatUtil;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.wxapi.WXEntryActivity;

import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-18 0018.
 * Withdraws Header
 */
public final class UserWithdrawalsHeaderView extends LinearLayout implements View.OnClickListener {
    TextView tvUserBalance;
    EditText etUserWithdrawals;
    TextView tvWithdrawalsMsg;
    TextView tvBindToWeChat;
    CircleImageView ivWeChatPortrait;
    TextView tvWeChatNickname;

    TextView tvChangeWeChat;
    TextView tvApplyToWithdrawals;

    ViewGroup vgApplyRecord;

    private boolean is_bind_to_wx;// 已经绑定微信
    private int balance;// 余额
    private String wx_name;// 微信昵称
    private String wx_portrait;// 微信头像

    private boolean isBindNewWeChat;
    private boolean isBindWeChatOnResume;

    private String applyCheck;// 防止重复申请审核

    public UserWithdrawalsHeaderView(Context context) {
        this(context, null);
    }

    public UserWithdrawalsHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserWithdrawalsHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializing(context);
    }

    private void initializing(Context context) {
        applyCheck = "";

        LayoutInflater.from(context).inflate(R.layout.layout_header_withdraws, this, true);

        tvUserBalance = (TextView) findViewById(R.id.tvUserBalance);
        etUserWithdrawals = (EditText) findViewById(R.id.etUserWithdrawals);

        tvWithdrawalsMsg = (TextView) findViewById(R.id.tvWithdrawalsMsg);
        tvBindToWeChat = (TextView) findViewById(R.id.tvBindToWeChat);
        ivWeChatPortrait = (CircleImageView) findViewById(R.id.ivWeChatPortrait);
        tvWeChatNickname = (TextView) findViewById(R.id.tvWeChatNickname);

        tvChangeWeChat = (TextView) findViewById(R.id.tvChangeWeChat);
        tvApplyToWithdrawals = (TextView) findViewById(R.id.tvApplyToWithdrawals);

        vgApplyRecord = (ViewGroup) findViewById(R.id.vgApplyRecord);

        tvBindToWeChat.setOnClickListener(this);
        tvChangeWeChat.setOnClickListener(this);
        tvApplyToWithdrawals.setOnClickListener(this);

        setOnClickListener(this);
    }

    public void setBindFlags(int balance, boolean is_bind_to_wx, String wx_name, String wx_portrait) {
        this.balance = balance;
        this.is_bind_to_wx = is_bind_to_wx;
        this.wx_name = wx_name;
        this.wx_portrait = wx_portrait;

        KLog.d(balance);
        KLog.d(is_bind_to_wx);
        KLog.d(wx_name);
        KLog.d(wx_portrait);

        // 设置余额
        tvUserBalance.setText(String.format(Locale.getDefault(), "%.2f", balance * 1F / 100F));
        tvUserBalance.append("元");

        // 绑定微信
        if (is_bind_to_wx) {
            tvWithdrawalsMsg.setText("您将提现到以下微信帐号");
            tvBindToWeChat.setVisibility(View.GONE);
            ivWeChatPortrait.setVisibility(View.VISIBLE);
            tvWeChatNickname.setVisibility(View.VISIBLE);
            tvChangeWeChat.setVisibility(View.VISIBLE);
            tvApplyToWithdrawals.setVisibility(View.VISIBLE);
            vgApplyRecord.setVisibility(View.VISIBLE);

            tvWeChatNickname.setText(wx_name);
            if (TextUtils.isEmpty(wx_portrait)) {
                ImageUtil.loadImage(ivWeChatPortrait, R.mipmap.icon_user_default);
            } else {
                ImageUtil.loadImage(ivWeChatPortrait, R.mipmap.icon_user_default, wx_portrait);
            }
        } else {
            tvWithdrawalsMsg.setText("首次提现请绑定微信");
            tvBindToWeChat.setVisibility(View.VISIBLE);
            ivWeChatPortrait.setVisibility(View.GONE);
            tvWeChatNickname.setVisibility(View.GONE);
            tvChangeWeChat.setVisibility(View.GONE);
            tvApplyToWithdrawals.setVisibility(View.GONE);
            vgApplyRecord.setVisibility(View.GONE);
        }
    }

    private void onBindWeChat(boolean isNew) {
        isBindNewWeChat = isNew;
        isBindWeChatOnResume = true;
        WXEntryActivity.REQUEST_TAG = 3;
        WeChatUtil.weChatLogin(getContext());
    }

    public void setBindWeChat(final String openid, final String wx_name, final String wx_portrait) {
        if (!isBindWeChatOnResume)
            return;

        final Context context = getContext();

        WeChatUtil.setOpenId(context, openid);

        HashMap<String, String> map = new HashMap<>();
        if (UserInfoUtil.checkLogin(context)) {
            map.put("user", UserInfoUtil.getUserId(context));
            map.put("token", UserInfoUtil.getUserToken(context));
        }
        map.put("openid", openid);
        map.put("op", isBindNewWeChat ? "new" : "update");
        map.put("wx_name", wx_name);
        map.put("wx_portrait", wx_portrait);

        final LoadingProgressDialog dialog = new LoadingProgressDialog(context);
        dialog.show();
        HttpClientUtil.getHttpClientUtil().sendPostRequest("UserWithdrawsHeaderView", HttpUrls.HOST_URL_V5 + "user/bind_wechat/", map, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(context, R.string.request_error);
            }

            @Override
            public void onSuccess(Call call, String response) {
                JSONObject object = JSONObject.parseObject(response);
                if (JsonParams.isJsonRight(object)) {
                    setBindFlags(balance, true, wx_name, wx_portrait);
                    ToastUtil.show(context, "绑定成功");
                } else {
                    ToastUtil.show(context, object.getString(JsonParams.MESSAGE));
                }
            }

            @Override
            public void onFinish(Call call) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == this) {
            try {
                SoftInputUtil.hideSoftInput((Activity) v.getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        switch (v.getId()) {
            case R.id.tvBindToWeChat:
                onBindWeChat(true);
                break;
            case R.id.tvChangeWeChat:
                onBindWeChat(false);
                break;
            case R.id.tvApplyToWithdrawals:
                if (TextUtils.isEmpty(applyCheck)) {
                    applyCheck = getTimeMillisNumber();
                }
                applyToWithdrawals();
                break;
        }
    }

    public String getTimeMillisNumber() {

        StringBuilder sb = new StringBuilder();

        sb.append(System.currentTimeMillis());
        while (sb.length() < 32) {
            sb.append((int) (Math.random() * 10));
        }

        return sb.toString();
    }

    private void applyToWithdrawals() {
        final Context context = getContext();

        int amount = 0;
        try {
            amount = (int) (Float.parseFloat(etUserWithdrawals.getText().toString()) * 100);
        } catch (NumberFormatException ignored) {
        }
        if (amount <= 0) {
            ToastUtil.show(context, "请输入正确的提现金额");
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        if (UserInfoUtil.checkLogin(context)) {
            map.put("user", UserInfoUtil.getUserId(context));
            map.put("token", UserInfoUtil.getUserToken(context));
        }
        map.put("amount", String.valueOf(amount));
        map.put("repeat_token", applyCheck);

        HttpClientUtil.getHttpClientUtil().sendPostRequest("UserWithdrawsHeaderView", HttpUrls.HOST_URL_V5 + "user/user_apply_for_withdraw/", map, new HttpClientUtil.ProgressResponseCallBack() {
            @Override
            public void loadingProgress(int progress) {
            }

            @Override
            public void onBefore(Request request) {
            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(context, R.string.request_error);
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);

                try {
                    if (JsonParams.isJsonRight(response)) {
                        ToastUtil.show(context, "申请成功");
                        EventObject eventObject = new EventObject();
                        eventObject.getData().putString("apply", "success");
                        eventObject.addReceiver(UserWithdrawsActivity.class);
                        EventObject.postEventObject(eventObject, "user_reward");
                    } else {
                        JSONObject object = JSON.parseObject(response);
                        ToastUtil.show(context, object.getString(JsonParams.MESSAGE));
                    }
                } catch (NullPointerException e) {
                    ToastUtil.show(context, "申请失败");
                }
            }

            @Override
            public void onFinish(Call call) {
                applyCheck = "";
            }
        });
    }

    public final boolean isBindToWX() {
        return is_bind_to_wx;
    }
}
