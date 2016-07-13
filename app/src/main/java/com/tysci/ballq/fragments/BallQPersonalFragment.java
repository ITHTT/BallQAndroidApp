package com.tysci.ballq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.UserAccountActivity;
import com.tysci.ballq.activitys.UserAchievementActivity;
import com.tysci.ballq.activitys.UserAttentionActivity;
import com.tysci.ballq.activitys.UserBettingGuessRecordActivity;
import com.tysci.ballq.activitys.UserCollectionRecordActivity;
import com.tysci.ballq.activitys.UserMessageRecordActivity;
import com.tysci.ballq.activitys.UserTrendStatisticActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.modles.event.EventType;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQPersonalFragment extends BaseFragment{
    @Bind(R.id.iv_user_header)
    protected CircleImageView ivUserHeader;
    @Bind(R.id.iv_user_v)
    protected ImageView ivUserV;
    @Bind(R.id.iv_user_achievement_01)
    protected ImageView ivUserAchievement01;
    @Bind(R.id.iv_user_achievement_02)
    protected ImageView ivUserAchievement02;
    @Bind(R.id.tv_user_name)
    protected TextView tvUserName;
    @Bind(R.id.tv_user_bio)
    protected TextView tvUserBio;
    @Bind(R.id.tv_ROI)
    protected TextView tvROI;
    @Bind(R.id.tv_total_profi_and_loss)
    protected TextView tvTotalROI;
    @Bind(R.id.tv_winning_probability)
    protected TextView tvWins;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_personal;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        if(UserInfoUtil.checkLogin(baseActivity)){
            setUserInfo(UserInfoUtil.getUserInfo(baseActivity));
            UserInfoUtil.getUserInfo(baseActivity,Tag, UserInfoUtil.getUserId(baseActivity),false,null);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isCancledEventBus() {
        return false;
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {
        if(action.equals(EventType.EVENT_REFRESH_USER_INFO)){

        }
    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity) {
        super.userLogin(userInfoEntity);
        setUserInfo(userInfoEntity);

    }

    @Override
    protected void userExit() {
        super.userExit();
    }

    private void setUserInfo(UserInfoEntity userInfo){
        if(userInfo!=null){
            GlideImageLoader.loadImage(this,userInfo.getPt(), R.mipmap.icon_user_default,ivUserHeader);
            UserInfoUtil.setUserHeaderVMark(userInfo.getIsv(),ivUserV,ivUserHeader);
            if(TextUtils.isEmpty(userInfo.getTitle1())){
                ivUserAchievement01.setVisibility(View.GONE);
            }else{
                ivUserAchievement01.setVisibility(View.VISIBLE);
                GlideImageLoader.loadImage(this,userInfo.getTitle1(), R.mipmap.icon_user_achievement_circle_mark,ivUserAchievement01);
            }
            if(TextUtils.isEmpty(userInfo.getTitle2())){
                ivUserAchievement02.setVisibility(View.GONE);
            }else{
                ivUserAchievement02.setVisibility(View.VISIBLE);
                GlideImageLoader.loadImage(this,userInfo.getTitle2(), R.mipmap.icon_user_achievement_circle_mark,ivUserAchievement02);
            }
            tvUserName.setText(userInfo.getFname());
            if(TextUtils.isEmpty(userInfo.getBio())){
                tvUserBio.setVisibility(View.GONE);
            }else{
                tvUserBio.setVisibility(View.VISIBLE);
                tvUserBio.setText(userInfo.getBio());
            }
            tvROI.setText(String.format(Locale.getDefault(),"%.2f",userInfo.getRor())+"%");
            tvTotalROI.setText(String.format(Locale.getDefault(),"%.2f",(float)userInfo.getTearn()/100));
            tvWins.setText(String.format(Locale.getDefault(),"%.2f",userInfo.getWins()*100)+"%");
        }
    }

    @OnClick({R.id.iv_user_header,R.id.tv_user_name,R.id.tv_user_bio})
    protected void onClickUserInfo(View view){
       if(!UserInfoUtil.checkLogin(baseActivity)){
           UserInfoUtil.userLogin(baseActivity);
       }
    }

    @OnClick({R.id.menu_user_trend_statistics,
              R.id.menu_user_guessing_record,
              R.id.menu_user_message,
              R.id.menu_user_collection,
              R.id.menu_user_asset,
              R.id.menu_user_attentions,
              R.id.menu_user_achievement,
              R.id.menu_user_ball_warp_record,
              R.id.menu_user_old_guess_record})
    protected void onClickUserInfoMenuItem(View view){
        if(!UserInfoUtil.checkLogin(baseActivity)){
            UserInfoUtil.userLogin(baseActivity);
        }else{
            Class cls=null;
            switch (view.getId()){
                case R.id.menu_user_trend_statistics:
                    cls=UserTrendStatisticActivity.class;
                    break;
                case R.id.menu_user_guessing_record:
                    cls=UserBettingGuessRecordActivity.class;
                    break;
                case R.id.menu_user_collection:
                    cls=UserCollectionRecordActivity.class;
                    break;
                case R.id.menu_user_message:
                    cls=UserMessageRecordActivity.class;
                    break;
                case R.id.menu_user_attentions:
                    cls=UserAttentionActivity.class;
                    break;
                case R.id.menu_user_asset:
                    cls=UserAccountActivity.class;
                    break;
                case  R.id.menu_user_achievement:
                    cls=UserAchievementActivity.class;
                    break;
            }
            if(cls!=null){
                Intent intent=new Intent(baseActivity,cls);
                startActivity(intent);
            }
        }
    }
}
