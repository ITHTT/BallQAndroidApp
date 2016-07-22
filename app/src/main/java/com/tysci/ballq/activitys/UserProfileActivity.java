package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQUserRankInfoAdapter;
import com.tysci.ballq.views.adapters.BallQUserRewardRankInfoAdapter;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.views.widgets.MainMenuItemView;

import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/6/16.
 */
public class UserProfileActivity extends BaseActivity
{
    @Bind(R.id.ivUserIcon)
    protected CircleImageView ivUserIcon;
    @Bind(R.id.isV)
    protected ImageView isV;
    @Bind(R.id.ivAchievement1)
    protected ImageView ivAchievement1;
    @Bind(R.id.ivAchievement2)
    protected ImageView ivAchievement2;
    @Bind(R.id.tvUserNickName)
    protected TextView tvUserNickName;
    @Bind(R.id.tvUserBio)
    protected TextView tvUserBio;
    @Bind(R.id.tvAttention)
    protected TextView tvAttention;
    @Bind(R.id.tv_ROI)
    protected TextView tvROI;
    @Bind(R.id.tv_total_profi_and_loss)
    protected TextView tvTotalProfitAndLoss;
    @Bind(R.id.tv_winning_probability)
    protected TextView tvWinProbability;

    private int uid;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_ballq_user_profile;
    }

    @Override
    protected void initViews()
    {
        setTitle("用户资料");

    }

    @Override
    protected View getLoadingTargetView()
    {
        return this.findViewById(R.id.layout_user_info);
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        uid = intent.getIntExtra(Tag, -1);
        if (uid != -1)
        {
            showLoading();
            getUserInfo(uid);
        }
    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState)
    {

    }

    @Override
    protected void handleInstanceState(Bundle outState)
    {

    }

    @Override
    protected void onViewClick(View view)
    {

    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {

    }

    private void getUserInfo(int userId)
    {
        String url = HttpUrls.HOST_URL_V5 + "user/" + userId + "/profile/";
        HashMap<String, String> params = null;
        if (UserInfoUtil.checkLogin(this))
        {
            params = new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {
                showErrorInfo(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showLoading();
                        getUserInfo(uid);
                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                hideLoad();
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null)
                    {
                        UserInfoEntity userInfoEntity = obj.getObject("data", UserInfoEntity.class);
                        if (userInfoEntity != null)
                        {
                            showUserInfo(userInfoEntity);
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call)
            {

            }
        });
    }

    private void showUserInfo(UserInfoEntity userInfo)
    {
        GlideImageLoader.loadImage(this, userInfo.getPt(), R.mipmap.icon_user_default, ivUserIcon);
        UserInfoUtil.setUserHeaderVMark(userInfo.getIsv(), isV, ivUserIcon);
        UserInfoUtil.setUserAchievementInfo(this, userInfo.getTitle1(), ivAchievement1, userInfo.getTitle2(), ivAchievement2);
        tvUserNickName.setText(userInfo.getFname());
        if (TextUtils.isEmpty(userInfo.getBio()))
        {
            tvUserBio.setVisibility(View.GONE);
        }
        else
        {
            tvUserBio.setVisibility(View.VISIBLE);
            tvUserBio.setText(userInfo.getBio());
        }
        if (String.valueOf(uid).equals(UserInfoUtil.getUserId(this)))
        {
            tvAttention.setVisibility(View.GONE);
        }
        else
        {
            tvAttention.setVisibility(View.VISIBLE);
            tvAttention.setText(userInfo.getIsf() == 1 ? "取消关注" : "加关注");
        }
        tvROI.setText(String.format(Locale.getDefault(), "%.2f", userInfo.getRor()) + "%");
        ;
        tvTotalProfitAndLoss.setText(String.format(Locale.getDefault(), "%.2f", (float) userInfo.getTearn() / 100));
        tvWinProbability.setText(String.format(Locale.getDefault(), "%.2f", userInfo.getWins() * 100) + "%");

        View userTipOffRecord = this.findViewById(R.id.menu_user_tip_off_record);
        View userBallWrapRecord = this.findViewById(R.id.menu_user_ball_wrap_record);
        View oldUserBettingRecord = this.findViewById(R.id.menu_user_old_guess_record);

        View divider01 = this.findViewById(R.id.divider01);
        View divider02 = this.findViewById(R.id.divider02);
        View divider03 = this.findViewById(R.id.divider03);

        if (userInfo.getRank() >= 10)
        {
            userTipOffRecord.setVisibility(View.VISIBLE);
            userBallWrapRecord.setVisibility(View.VISIBLE);
            divider01.setVisibility(View.VISIBLE);
            divider02.setVisibility(View.VISIBLE);
        }
        else
        {
            userTipOffRecord.setVisibility(View.GONE);
            userBallWrapRecord.setVisibility(View.GONE);
            divider01.setVisibility(View.GONE);
            divider02.setVisibility(View.GONE);
        }
        if (userInfo.getIs_old_user() == 1)
        {
            oldUserBettingRecord.setVisibility(View.VISIBLE);
            divider03.setVisibility(View.VISIBLE);
        }
        else
        {
            oldUserBettingRecord.setVisibility(View.GONE);
            divider03.setVisibility(View.GONE);
        }
    }

    private void setClickMenuItem(View view)
    {
        LinearLayout layoutUserMenus = (LinearLayout) this.findViewById(R.id.layout_user_menus);
        int size = layoutUserMenus.getChildCount();
        for (int i = 0; i < size; i++)
        {
            View v = layoutUserMenus.getChildAt(i);
            if (v instanceof MainMenuItemView)
            {
                ((MainMenuItemView) v).setCheckedState(v == view);
            }
        }

    }

    @OnClick({R.id.menu_user_trend_statistics, R.id.menu_user_guessing_record, R.id.menu_user_attentions, R.id.menu_user_tip_off_record
            , R.id.menu_user_ball_wrap_record, R.id.menu_user_achievement, R.id.menu_user_old_guess_record})
    protected void onClickMenuItem(View view)
    {
        setClickMenuItem(view);
        Intent intent = null;
        switch (view.getId())
        {
            case R.id.menu_user_trend_statistics:// 走势统计
                intent = new Intent(this, UserTrendStatisticActivity.class);
                intent.putExtra(UserTrendStatisticActivity.class.getSimpleName(), String.valueOf(uid));
                break;
            case R.id.menu_user_guessing_record:// 竞猜记录
                intent = new Intent(this, UserBettingGuessRecordActivity.class);
                intent.putExtra(UserBettingGuessRecordActivity.class.getSimpleName(), String.valueOf(uid));
                break;
            case R.id.menu_user_tip_off_record:// 爆料记录
                intent = new Intent(this, UserTipOffListRecordActivity.class);
                intent.putExtra("uid", String.valueOf(uid));
                break;
            case R.id.menu_user_ball_wrap_record:// 球茎记录
                intent = new Intent(this, UserArticleListRecordActivity.class);
                intent.putExtra("uid", String.valueOf(uid));
                break;
            case R.id.menu_user_attentions:// 关注的人
                intent = new Intent(this, UserAttentionActivity.class);
                intent.putExtra(UserAttentionActivity.class.getSimpleName(), String.valueOf(uid));
                break;
            case R.id.menu_user_achievement:// 他的成就
                intent = new Intent(this, UserAchievementActivity.class);
                intent.putExtra("uid", String.valueOf(uid));
                break;
            case R.id.menu_user_old_guess_record:// 老用户战绩
                intent = new Intent(this, UserOldDataActivity.class);
                intent.putExtra("uid", String.valueOf(uid));
                break;
        }
        if (intent != null)
        {
            startActivity(intent);
        }
    }


    @OnClick(R.id.tvAttention)
    protected void userAttention(View view)
    {
        if (UserInfoUtil.checkLogin(this))
        {
            String url = HttpUrls.HOST_URL_V5 + "follow/change/";
            HashMap<String, String> params = new HashMap<>(4);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
            params.put("fid", String.valueOf(uid));
            String str = tvAttention.getText().toString();
            if (str.equals("加关注"))
            {
                params.put("change", "1");
            }
            else
            {
                params.put("change", "0");
            }
            HttpClientUtil.getHttpClientUtil().sendPostRequest(BallQUserRankingListDetailActivity.class.getSimpleName(), url, params, new HttpClientUtil.StringResponseCallBack()
            {
                @Override
                public void onBefore(Request request)
                {

                }

                @Override
                public void onError(Call call, Exception error)
                {
                    ToastUtil.show(UserProfileActivity.this, "请求失败");
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
                            ToastUtil.show(UserProfileActivity.this, obj.getString("message"));
                            if (status == 350)
                            {
                                tvAttention.setText("取消关注");
                                publishUserAttention(1);
                            }
                            else if (status == 352)
                            {
                                tvAttention.setText("加关注");
                                publishUserAttention(0);
                            }
                        }
                    }
                }

                @Override
                public void onFinish(Call call)
                {

                }
            });

        }
        else
        {
            UserInfoUtil.userLogin(this);
        }
    }

    private void publishUserAttention(int mark)
    {
        EventObject eventObject = new EventObject();
        eventObject.addReceiver(BallQUserRankInfoAdapter.class, BallQUserRewardRankInfoAdapter.class);
        eventObject.getData().putInt("attention", mark);
        eventObject.getData().putInt("uid", uid);
        EventObject.postEventObject(eventObject, "user_attention");
    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity)
    {
        super.userLogin(userInfoEntity);
        showLoading();
        getUserInfo(uid);
    }
}
