package com.tysci.ballq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.UserAccountActivity;
import com.tysci.ballq.activitys.UserAchievementActivity;
import com.tysci.ballq.activitys.UserArticleListRecordActivity;
import com.tysci.ballq.activitys.UserAttentionActivity;
import com.tysci.ballq.activitys.UserBettingGuessRecordActivity;
import com.tysci.ballq.activitys.UserCollectionRecordActivity;
import com.tysci.ballq.activitys.UserMessageRecordActivity;
import com.tysci.ballq.activitys.UserOldDataActivity;
import com.tysci.ballq.activitys.UserTipOffListRecordActivity;
import com.tysci.ballq.activitys.UserTrendStatisticActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.modles.event.EventType;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.UserProfileHeaderView;
import com.tysci.ballq.views.widgets.MainMenuItemView;
import com.tysci.ballq.views.widgets.TitleBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by HTT on 2016/7/12.
 *
 * @author Edit LinDe
 */
public class BallQPersonalFragment extends BaseFragment
{
    @Bind(R.id.title_bar)
    protected TitleBar titleBar;
//    @Bind(R.id.iv_user_header)
//    protected CircleImageView ivUserHeader;
//    @Bind(R.id.iv_user_v)
//    protected ImageView ivUserV;
//    @Bind(R.id.iv_user_achievement_01)
//    protected ImageView ivUserAchievement01;
//    @Bind(R.id.iv_user_achievement_02)
//    protected ImageView ivUserAchievement02;
//    @Bind(R.id.tv_user_name)
//    protected TextView tvUserName;
//    @Bind(R.id.tv_user_bio)
//    protected TextView tvUserBio;
//    @Bind(R.id.tv_ROI)
//    protected TextView tvROI;
//    @Bind(R.id.tv_total_profi_and_loss)
//    protected TextView tvTotalROI;
//    @Bind(R.id.tv_winning_probability)
//    protected TextView tvWins;

    @Bind(R.id.user_profile_header_view)
    protected UserProfileHeaderView mUserProfileHeaderView;

    @Bind(R.id.menu_user_tip_off_record)
    protected MainMenuItemView tipRecord;
    @Bind(R.id.menu_user_ball_warp_record)
    protected MainMenuItemView articleRecord;
    @Bind(R.id.menu_user_old_guess_record)
    protected MainMenuItemView oldUser;

    @Override
    protected int getViewLayoutId()
    {
        return R.layout.fragment_ballq_personal;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState)
    {
        titleBar.setTitleBarTitle("我的");
        titleBar.setTitleBarLeftIcon(0, null);
        if (UserInfoUtil.checkLogin(baseActivity))
        {
            setUserInfo(UserInfoUtil.getUserInfo(baseActivity));
            UserInfoUtil.getUserInfo(baseActivity, Tag, UserInfoUtil.getUserId(baseActivity), false, null);

            if (UserInfoUtil.isVIPUser(baseActivity))
            {
                tipRecord.setVisibility(View.VISIBLE);
                articleRecord.setVisibility(View.VISIBLE);
            }
            else
            {
                tipRecord.setVisibility(View.GONE);
                articleRecord.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected View getLoadingTargetView()
    {
        return null;
    }

    @Override
    protected boolean isCancledEventBus()
    {
        return false;
    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {
        if (action.equals(EventType.EVENT_USER_EXIT))
        {
//            ivUserHeader.setImageResource(R.mipmap.icon_camera);
//            ivUserV.setVisibility(View.GONE);
//            tvUserName.setText("登录后即可参与竞猜");
            mUserProfileHeaderView.setUserUnLoginProfile(UserInfoUtil.getUserId(baseActivity));
        }
    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity)
    {
        super.userLogin(userInfoEntity);
        setUserInfo(userInfoEntity);

    }

    @Override
    protected void userExit()
    {
        super.userExit();
    }

    private void setUserInfo(UserInfoEntity userInfo)
    {
        if (userInfo != null)
        {
//            GlideImageLoader.loadImage(this, userInfo.getPt(), R.mipmap.icon_user_default, ivUserHeader);
//            UserInfoUtil.setUserHeaderVMark(userInfo.getIsv(), ivUserV, ivUserHeader);
//            if (TextUtils.isEmpty(userInfo.getTitle1()))
//            {
//                ivUserAchievement01.setVisibility(View.GONE);
//            }
//            else
//            {
//                ivUserAchievement01.setVisibility(View.VISIBLE);
//                GlideImageLoader.loadImage(this, userInfo.getTitle1(), R.mipmap.icon_user_achievement_circle_mark, ivUserAchievement01);
//            }
//            if (TextUtils.isEmpty(userInfo.getTitle2()))
//            {
//                ivUserAchievement02.setVisibility(View.GONE);
//            }
//            else
//            {
//                ivUserAchievement02.setVisibility(View.VISIBLE);
//                GlideImageLoader.loadImage(this, userInfo.getTitle2(), R.mipmap.icon_user_achievement_circle_mark, ivUserAchievement02);
//            }
//            tvUserName.setText(userInfo.getFname());
//            if (TextUtils.isEmpty(userInfo.getBio()))
//            {
//                tvUserBio.setVisibility(View.GONE);
//            }
//            else
//            {
//                tvUserBio.setVisibility(View.VISIBLE);
//                tvUserBio.setText(userInfo.getBio());
//            }
            // 老用户
            oldUser.setVisibility(userInfo.getIs_old_user() == 1 ? View.VISIBLE : View.GONE);

//            tvROI.setText(String.format(Locale.getDefault(), "%.2f", userInfo.getRor()));
//            tvROI.append("%");
//            tvTotalROI.setText(String.format(Locale.getDefault(), "%.2f", (float) userInfo.getTearn() / 100));
//            tvWins.setText(String.format(Locale.getDefault(), "%.2f", userInfo.getWins() * 100));
//            tvWins.append("%");

            mUserProfileHeaderView.setUserProfile(userInfo);
        }
    }

//    @OnClick({R.id.iv_user_header, R.id.tv_user_name, R.id.tv_user_bio})
//    protected void onClickUserInfo(View view)
//    {
//        if (!UserInfoUtil.checkLogin(baseActivity))
//        {
//            UserInfoUtil.userLogin(baseActivity);
//        }
//    }

    @OnClick({R.id.menu_user_trend_statistics,
            R.id.menu_user_guessing_record,
            R.id.menu_user_message,
            R.id.menu_user_collection,
            R.id.menu_user_asset,
            R.id.menu_user_attentions,
            R.id.menu_user_achievement,
            R.id.menu_user_tip_off_record,
            R.id.menu_user_ball_warp_record,
            R.id.menu_user_old_guess_record})
    protected void onClickUserInfoMenuItem(View view)
    {
        if (!UserInfoUtil.checkLogin(baseActivity))
        {
            UserInfoUtil.userLogin(baseActivity);
        }
        else
        {
            Class cls = null;
            switch (view.getId())
            {
                case R.id.menu_user_trend_statistics:// 走势统计
                    cls = UserTrendStatisticActivity.class;
                    break;
                case R.id.menu_user_guessing_record:// 竞猜记录
                    cls = UserBettingGuessRecordActivity.class;
                    break;
                case R.id.menu_user_collection:
                    cls = UserCollectionRecordActivity.class;
                    break;
                case R.id.menu_user_message:
                    cls = UserMessageRecordActivity.class;
                    break;
                case R.id.menu_user_attentions:
                    cls = UserAttentionActivity.class;
                    break;
                case R.id.menu_user_asset:// 账户中心
                    cls = UserAccountActivity.class;
                    break;
                case R.id.menu_user_achievement:// 我的成就
                    cls = UserAchievementActivity.class;
                    break;
                case R.id.menu_user_tip_off_record:// 爆料记录
                    cls = UserTipOffListRecordActivity.class;
                    break;
                case R.id.menu_user_ball_warp_record:// 球茎记录
                    cls = UserArticleListRecordActivity.class;
                    break;
                case R.id.menu_user_old_guess_record:// 老用户战绩
                    cls = UserOldDataActivity.class;
                    break;
//                case R.id.menu_setting:// 设置
//                    cls = BallQSettingActivity.class;
//                    break;
            }
            if (cls != null)
            {
                Intent intent = new Intent(baseActivity, cls);
                startActivity(intent);
            }
        }
    }
}
