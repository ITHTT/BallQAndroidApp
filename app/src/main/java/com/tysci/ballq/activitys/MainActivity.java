package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.fragments.BallQHomePageFragment;
import com.tysci.ballq.fragments.BallQMatchFragment;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.modles.event.EventType;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.views.widgets.MainMenuItemView;
import com.tysci.ballq.views.widgets.slidingmenu.SlidingMenu;
import com.tysci.ballq.wxapi.WXEntryActivity;

import java.util.Locale;

public class MainActivity extends BaseActivity {
    private SlidingMenu slidingMenu=null;
    private View mainLeftMenu;
    private View mainRightMenu;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        initSlidingMenu();
        setTitleBarLeftIcon(R.mipmap.icon_main_left_menu);
        titleBar.setRightMenuIcon(R.mipmap.icon_main_right_menu, this);
        addMenusItemOnClickListener();
        setSelectedLeftMenuItem(-1);

        if(UserInfoUtil.checkLogin(this)){
            showUserInfo(UserInfoUtil.getUserInfo(this));
            UserInfoUtil.getUserInfo(this,Tag, UserInfoUtil.getUserId(this),false,null);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCH_MODE_MARGIN);
        slidingMenu.setShadowWidth(200);
//        menu.setShadowDrawable(R.drawable.shadow);

        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindOffset(CommonUtils.getScreenDisplayMetrics(this).widthPixels/4);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.5f);
        mainLeftMenu= LayoutInflater.from(this).inflate(R.layout.layout_main_left_menu,null);
        mainRightMenu=LayoutInflater.from(this).inflate(R.layout.layout_main_right_menu,null);
        slidingMenu.setMenu(mainLeftMenu);
        slidingMenu.setSecondaryMenu(mainRightMenu);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        mainRightMenu.findViewById(R.id.iv_user_header).setOnClickListener(this);
        mainRightMenu.findViewById(R.id.tv_user_name).setOnClickListener(this);
    }

    /**
     * 为左右菜单项添加点击事件
     */
    private void addMenusItemOnClickListener(){
        LinearLayout layoutLeftMenus= (LinearLayout) mainLeftMenu.findViewById(R.id.layout_left_menus);
        int size=layoutLeftMenus.getChildCount();
        for(int i=0;i<size;i++){
            View view=layoutLeftMenus.getChildAt(i);
            if(view instanceof MainMenuItemView){
                view.setOnClickListener(this);
            }
        }

        LinearLayout layoutRightMenus=(LinearLayout)mainRightMenu.findViewById(R.id.layout_right_menus);
        size=layoutRightMenus.getChildCount();
        for(int i=0;i<size;i++){
            View view=layoutRightMenus.getChildAt(i);
            if(view instanceof MainMenuItemView){
                view.setOnClickListener(this);
            }
        }
    }

    private void onMenuItemClick(View view){
        if(view instanceof MainMenuItemView) {
            LinearLayout layoutRightMenus = (LinearLayout) mainRightMenu.findViewById(R.id.layout_right_menus);
            int size = layoutRightMenus.getChildCount();
            for (int i = 0; i < size; i++) {
                View v = layoutRightMenus.getChildAt(i);
                if (v instanceof MainMenuItemView) {
                    if(v==view) {
                        slidingMenu.showSecondaryMenu();
                        setSelectedRightMenuItem(view.getId());
                        return;
                    }
                }
            }

            LinearLayout layoutLeftMenus = (LinearLayout) mainLeftMenu.findViewById(R.id.layout_left_menus);
            size = layoutLeftMenus.getChildCount();
            for (int i = 0; i < size; i++) {
                View v = layoutLeftMenus.getChildAt(i);
                if (v instanceof MainMenuItemView) {
                    ((MainMenuItemView) v).setCheckedState(v == view);
                    if(v==view){
                        slidingMenu.toggle();
                        setSelectedLeftMenuItem(view.getId());
                    }
                }
            }
        }
    }

    private void setSelectedLeftMenuItem(int id){
        BaseFragment fragment=null;
        switch (id){
            case -1:
                fragment=new BallQHomePageFragment();
                break;
//            case R.id.menu_index:
//                fragment=new BallQHomeFragment();
//                break;
            case R.id.menu_match:
                fragment=new BallQMatchFragment();
                break;
        }

        if(fragment!=null){
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.layout_container,fragment,fragment.getClass().getSimpleName());
            transaction.commitAllowingStateLoss();
        }
    }

    private void setSelectedRightMenuItem(int id){
        if(!UserInfoUtil.checkLogin(this)){
            UserInfoUtil.userLogin(this);
        }else{
            Class cls=null;
            switch (id){
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
            }
            if(cls!=null){
                Intent intent=new Intent(this,cls);
                startActivity(intent);
            }
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
    protected void back() {
        slidingMenu.toggle(true);
    }

    @Override
    protected void handleInstanceState(Bundle outState) {

    }

    private void goLogin(){
        Intent intent=new Intent(this, WXEntryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onViewClick(View view) {
        onMenuItemClick(view);
        int id=view.getId();
        switch(id){
            case R.id.iv_titlebar_next_menu01:
                slidingMenu.showSecondaryMenu();
                break;
            case R.id.iv_user_header:
                if(UserInfoUtil.checkLogin(this)){

                }else{
                    goLogin();
                }
                break;
            case R.id.tv_user_name:
                if(UserInfoUtil.checkLogin(this)){

                }else{
                    goLogin();
                }
                break;
        }
    }

    /**
     * 显示用户信息
     * @param userInfo
     */
    private void showUserInfo(UserInfoEntity userInfo){
        if(userInfo!=null) {
            CircleImageView ivUserHeader = (CircleImageView) mainRightMenu.findViewById(R.id.iv_user_header);
            GlideImageLoader.loadImage(this,userInfo.getPt(), R.mipmap.icon_user_default,ivUserHeader);
            ImageView ivMark=(ImageView)mainRightMenu.findViewById(R.id.iv_user_v);
            UserInfoUtil.setUserHeaderVMark(userInfo.getIsv(),ivMark,ivUserHeader);
            ImageView ivUserAchievement01= (ImageView) mainRightMenu.findViewById(R.id.iv_user_achievement_01);
            ImageView ivUserAchievement02=(ImageView)mainRightMenu.findViewById(R.id.iv_user_achievement_02);
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
            TextView tvUserName=(TextView)mainRightMenu.findViewById(R.id.tv_user_name);
            tvUserName.setText(userInfo.getFname());
            TextView tvUserBio=(TextView)mainRightMenu.findViewById(R.id.tv_user_bio);
            if(TextUtils.isEmpty(userInfo.getBio())){
                tvUserBio.setVisibility(View.GONE);
            }else{
                tvUserBio.setVisibility(View.VISIBLE);
                tvUserBio.setText(userInfo.getBio());
            }
            TextView tvUserROI=(TextView)mainRightMenu.findViewById(R.id.tv_ROI);
            tvUserROI.setText(String.format(Locale.getDefault(),"%.2f",userInfo.getRor())+"%");
            TextView tvUserLossAndProfit=(TextView)mainRightMenu.findViewById(R.id.tv_total_profi_and_loss);
            tvUserLossAndProfit.setText(String.format(Locale.getDefault(),"%.2f",(float)userInfo.getTearn()/100));
            TextView tvUserWinRate=(TextView)mainRightMenu.findViewById(R.id.tv_winning_probability);
            tvUserWinRate.setText(String.format(Locale.getDefault(),"%.2f",userInfo.getWins()*100)+"%");
        }
    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity) {
        showUserInfo(userInfoEntity);
    }

    @Override
    protected void userExit() {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {
        if(action.equals(EventType.EVENT_REFRESH_USER_INFO)){

        }
    }
}
