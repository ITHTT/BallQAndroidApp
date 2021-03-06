package com.tysci.ballq.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQSettingActivity;
import com.tysci.ballq.activitys.BallQUserRankingListDetailActivity;
import com.tysci.ballq.dialog.ImageUrlBrowserDialog;
import com.tysci.ballq.dialog.SpinKitProgressDialog;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQUserRankInfoAdapter;
import com.tysci.ballq.views.adapters.BallQUserRewardRankInfoAdapter;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe
 * on 2016-07-26 0026.
 */
public final class UserProfileHeaderView extends LinearLayout implements View.OnClickListener
{
    private static final String IS_FOLLOWING = "取消关注";
    private static final String IS_UN_FOLLOW = "关注";

    @Bind(R.id.layout_user_info)
    ViewGroup layoutUserInfo;

    @Bind(R.id.iv_user_portrait)
    CircleImageView ivUserPortrait;// 用户头像
    @Bind(R.id.iv_user_v)
    ImageView ivUserV;// 用户V头像
    @Bind(R.id.tv_user_nickname)
    MarqueeTextView tvUserNickname;// 用户昵称
    @Bind(R.id.iv_expert)
    UserVStatusImageView ivUserExpert;// 砖家图标
    @Bind(R.id.tv_follow_click)
    TextView tvFollowClick;// 关注/取消关注
    @Bind(R.id.iv_user_achievement01)
    ImageView ivUserAchievement1;// 成就图标1
    @Bind(R.id.iv_user_achievement02)
    ImageView ivUserAchievement2;// 成就图标2
    @Bind(R.id.tv_user_bio)
    MarqueeTextView tvUserBio;// 用户签名
    @Bind(R.id.iv_setting)
    ImageView ivSetting;// 设置

    @Bind(R.id.tv_roi)
    MarqueeTextView tv_roi;// 总盈亏
    @Bind(R.id.tv_total_profit_and_loss)
    MarqueeTextView tv_total_profit_and_loss;// 投资回报
    @Bind(R.id.tv_winning_probability)
    MarqueeTextView tv_winning_probability;// 胜率

    @Bind(R.id.layout_user_betting_counts)
    ViewGroup layoutUserBettingCounts;// 场次视图
    @Bind(R.id.tv_all_count)
    MarqueeTextView tvAllCount;// 总场次
    @Bind(R.id.tv_win_count)
    MarqueeTextView tvWinCount;// 赢场
    @Bind(R.id.tv_lose_count)
    MarqueeTextView tvLoseCount;// 输场
    @Bind(R.id.tv_go_count)
    MarqueeTextView tvGoneCount;// 走场

    private String userId;
    private String mPortraitUrl;

    public UserProfileHeaderView(Context context)
    {
        this(context, null);
    }

    public UserProfileHeaderView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public UserProfileHeaderView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializing(context);
    }

    private void initializing(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_user_profile_header, this, true);

        ButterKnife.bind(this);

        setOnClickListener(this);
        setUserUnLoginProfile(null);
    }

    public void setUserUnLoginProfile(String userId)
    {
        this.userId = userId;
        mPortraitUrl = null;
        final boolean isUserSelf = TextUtils.isEmpty(userId) || userId.equals(UserInfoUtil.getUserId(getContext()));

        // 头像
        ivUserPortrait.setImageResource(R.mipmap.icon_user_default);
        UserInfoUtil.setUserHeaderVMark(0, ivUserV, ivUserPortrait);
        // 昵称
        tvUserNickname.setText(isUserSelf ? "登陆后即可参与竞猜" : "匿名用户");
        // 关注
        tvFollowClick.setVisibility(GONE);
        // 砖家图标
        ivUserExpert.setVisibility(GONE);
        // 成就图标1
        ivUserAchievement1.setVisibility(GONE);
        // 成就图标2
        ivUserAchievement2.setVisibility(GONE);
        // 用户签名
        tvUserBio.setVisibility(GONE);

        ivSetting.setVisibility(/*isUserSelf ? VISIBLE : */GONE);

        // 投资回报
        tv_roi.setText("+0%");
        // 总盈亏
        tv_total_profit_and_loss.setText("+0");
        // 胜率
        tv_winning_probability.setText("+0%");

//        if (isUserSelf)
//        {
        layoutUserBettingCounts.setVisibility(GONE);
        setUserInfoLayoutParams(93);
//        }
//        else
//        {
//            layoutUserBettingCounts.setVisibility(VISIBLE);
//            setUserInfoLayoutParams(125);
//        }
        // 总场次
        tvAllCount.setText(String.valueOf(0));
        tvAllCount.append("场");
        // 赢
        tvWinCount.setText(String.valueOf(0));
        tvWinCount.append("赢");
        // 输
        tvLoseCount.setText(String.valueOf(0));
        tvLoseCount.append("输");
        // 走
        tvGoneCount.setText(String.valueOf(0));
        tvGoneCount.append("走");
    }

    public void setUserProfile(UserInfoEntity userInfo)
    {
        if (userInfo == null)
            return;

        userId = String.valueOf(userInfo.getUid());
        mPortraitUrl = userInfo.getPt();
        final boolean isUserSelf = userId.equals(UserInfoUtil.getUserId(getContext()));

//        String tmp;
        float fmp;
        int imp;

        // 头像
        ImageUtil.loadImage(ivUserPortrait, R.mipmap.icon_user_default, userInfo.getPt());
        UserInfoUtil.setUserHeaderVMark(userInfo.getIsv(), ivUserV, ivUserPortrait);
        // 昵称
        tvUserNickname.setText(userInfo.getFname());
        // 砖家图标
        ivUserExpert.setUserV_Icon(userInfo.getIsv());
        // 关注
        tvFollowClick.setVisibility(isUserSelf ? GONE : VISIBLE);
//        // 成就图标1
//        tmp = userInfo.getTitle1();
//        if (!isUserSelf)
        ivUserAchievement1.setVisibility(GONE);
//        else if (TextUtils.isEmpty(tmp))
//            ivUserAchievement1.setVisibility(GONE);
//        else
//        {
//            ivUserAchievement1.setVisibility(VISIBLE);
//            ImageUtil.loadImage(ivUserAchievement1, R.mipmap.icon_user_achievement_circle_mark, tmp);
//        }
//        // 成就图标2
//        tmp = userInfo.getTitle2();
//        if (!isUserSelf)
        ivUserAchievement2.setVisibility(GONE);
//        else if (TextUtils.isEmpty(tmp))
//            ivUserAchievement2.setVisibility(GONE);
//        else
//        {
//            ivUserAchievement2.setVisibility(VISIBLE);
//            ImageUtil.loadImage(ivUserAchievement2, R.mipmap.icon_user_achievement_circle_mark, tmp);
//        }
//        // 用户签名
//        tmp = userInfo.getBio();
//        if (isUserSelf)
        tvUserBio.setVisibility(GONE);
//        else if (TextUtils.isEmpty(tmp))
//            tvUserBio.setVisibility(GONE);
//        else
//        {
//            tvUserBio.setVisibility(VISIBLE);
//            tvUserBio.setText(tmp);
//        }

        tvFollowClick.setText(userInfo.getIsf() == 1 ? IS_FOLLOWING : IS_UN_FOLLOW);

        ivSetting.setVisibility(isUserSelf ? VISIBLE : GONE);

        // 投资回报
        fmp = userInfo.getRor();
        tv_roi.setText(fmp >= 0 ? "+" : "");
        tv_roi.append(String.format(Locale.getDefault(), "%.2f", fmp));
        tv_roi.append("%");
        // 总盈亏
        fmp = userInfo.getTearn();
        tv_total_profit_and_loss.setText(fmp >= 0 ? "+" : "");
        tv_total_profit_and_loss.append(String.format(Locale.getDefault(), "%.2f", fmp * 1F / 100));
        // 胜率
        fmp = userInfo.getWins();
        tv_winning_probability.setText(fmp >= 0 ? "+" : "");
        tv_winning_probability.append(String.format(Locale.getDefault(), "%.2f", fmp * 100F));
        tv_winning_probability.append("%");

//        if (isUserSelf)
//        {
        layoutUserBettingCounts.setVisibility(GONE);
        setUserInfoLayoutParams(93);
//        }
//        else
//        {
//            layoutUserBettingCounts.setVisibility(VISIBLE);
//            setUserInfoLayoutParams(125);
//        }
        // 总场次
        imp = userInfo.getBsc();
        if (imp < 0)
            imp = 0;
        tvAllCount.setText(String.valueOf(imp));
        tvAllCount.append("场");
        // 赢
        imp = userInfo.getBwc();
        if (imp < 0)
            imp = 0;
        tvWinCount.setText(String.valueOf(imp));
        tvWinCount.append("赢");
        // 输
        imp = userInfo.getBlc();
        if (imp < 0)
            imp = 0;
        tvLoseCount.setText(String.valueOf(imp));
        tvLoseCount.append("输");
        // 走
        imp = userInfo.getBgc();
        if (imp < 0)
            imp = 0;
        tvGoneCount.setText(String.valueOf(imp));
        tvGoneCount.append("走");
    }

    private void setUserInfoLayoutParams(int height)
    {
        LayoutParams lp = (LayoutParams) layoutUserInfo.getLayoutParams();
        lp.height = CommonUtils.dip2px(getContext(), height);
        layoutUserInfo.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v)
    {
        final Context context = getContext();
        if (!UserInfoUtil.checkLogin(context))
        {
            if (TextUtils.isEmpty(userId))
                UserInfoUtil.userLogin(context);
        }
    }

    /**
     * 关注/取消关注
     */
    @OnClick(R.id.tv_follow_click)
    protected void onFollowClick(View view)
    {
        tvFollowClick.setEnabled(false);

        final Context context = getContext();

        if (!UserInfoUtil.checkLogin(context))
        {
            UserInfoUtil.userLogin(context);
            tvFollowClick.setEnabled(true);
            return;
        }
        String url = HttpUrls.HOST_URL_V5 + "follow/change/";
        HashMap<String, String> params = new HashMap<>(4);
        params.put("user", UserInfoUtil.getUserId(context));
        params.put("token", UserInfoUtil.getUserToken(context));
        params.put("fid", userId);
        String str = tvFollowClick.getText().toString();
        if (str.equals(IS_UN_FOLLOW))
        {
            params.put("change", "1");
        }
        else if (str.equals(IS_FOLLOWING))
        {
            params.put("change", "0");
        }
        final SpinKitProgressDialog dialog = new SpinKitProgressDialog((Activity) context);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(BallQUserRankingListDetailActivity.class.getSimpleName(), url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {
                //noinspection ConstantConditions
                if (dialog != null)
                    dialog.show();
            }

            @Override
            public void onError(Call call, Exception error)
            {
                ToastUtil.show(context, "请求失败");
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty())
                    {
                        int status = obj.getIntValue("status");
                        ToastUtil.show(context, obj.getString("message"));
                        if (status == 350)
                        {
                            tvFollowClick.setText(IS_FOLLOWING);
                            publishUserAttention(1);
                        }
                        else if (status == 352)
                        {
                            tvFollowClick.setText(IS_UN_FOLLOW);
                            publishUserAttention(0);
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call)
            {
                tvFollowClick.setEnabled(true);
                //noinspection ConstantConditions
                if (dialog != null)
                {
                    dialog.dismiss();
                }
            }
        });
    }

    private void publishUserAttention(int mark)
    {
        EventObject eventObject = new EventObject();
        eventObject.addReceiver(BallQUserRankInfoAdapter.class, BallQUserRewardRankInfoAdapter.class);
        eventObject.getData().putInt("attention", mark);
        eventObject.getData().putInt("uid", Integer.parseInt(userId));
        EventObject.postEventObject(eventObject, "user_attention");
    }

    /**
     * 设置
     */
    @OnClick(R.id.iv_setting)
    protected void onSettingClick(View view)
    {
        final Context context = getContext();

        Intent intent = new Intent(context, BallQSettingActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.iv_user_portrait)
    protected void onPortraitClick(View view)
    {
        final Context context = getContext();
        final boolean isUserIdEmpty = TextUtils.isEmpty(userId);

        if (!UserInfoUtil.checkLogin(context))
        {
            if (isUserIdEmpty)
            {
                UserInfoUtil.userLogin(context);
                return;
            }
        }
        if (!TextUtils.isEmpty(mPortraitUrl))
        {
            ImageUrlBrowserDialog dialog = new ImageUrlBrowserDialog((Activity) context);
            dialog.addUrl(mPortraitUrl);
            dialog.show();
        }
    }

    @OnClick(R.id.tv_user_nickname)
    protected void onUserNicknameClick(View view)
    {
        final Context context = getContext();
        final boolean isUserIdEmpty = TextUtils.isEmpty(userId);

//        ImageUrlBrowserDialog dialog = new ImageUrlBrowserDialog((Activity) context, R.mipmap.icon_user_default);
//        dialog.addData(mPortraitUrl, mPortraitUrl, mPortraitUrl, mPortraitUrl, mPortraitUrl, mPortraitUrl, mPortraitUrl, mPortraitUrl);
//        dialog.setCurrentImageIndex(50);
//        dialog.show();

        if (!UserInfoUtil.checkLogin(context))
        {
            if (isUserIdEmpty)
            {
                UserInfoUtil.userLogin(context);
            }
        }
        else if (!isUserIdEmpty && userId.equals(UserInfoUtil.getUserId(context)))
        {
            Intent intent = new Intent(context, BallQSettingActivity.class);
            context.startActivity(intent);
        }
    }
}
