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
        mUserProfileHeaderView.setUserUnLoginProfile(UserInfoUtil.getUserId(baseActivity));

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
            oldUser.setVisibility(userInfo.getIs_old_user() == 1 ? View.VISIBLE : View.GONE);

            mUserProfileHeaderView.setUserProfile(userInfo);
        }
    }

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
                case R.id.menu_user_collection:// 我的收藏
                    cls = UserCollectionRecordActivity.class;
                    break;
                case R.id.menu_user_message:// 我的消息
                    cls = UserMessageRecordActivity.class;
                    break;
                case R.id.menu_user_attentions:// 关注的人
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
