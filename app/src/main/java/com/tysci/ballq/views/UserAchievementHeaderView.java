package com.tysci.ballq.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.interfaces.ITabCheck;

/**
 * Created by Administrator on 2016-07-15 0015.
 *
 * @author LinDe
 */
public final class UserAchievementHeaderView extends LinearLayout implements View.OnClickListener {
    private TextView tv_had_got_achievement;// 已选择展示的成就提示
    private ViewGroup layout_showing_achievement;// 展示的成就

    private TextView tv_left;
    private TextView tv_right;

    private ITabCheck tabCheck;

    public UserAchievementHeaderView(Context context) {
        this(context, null);
    }

    public UserAchievementHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserAchievementHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializing(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UserAchievementHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializing(context);
    }

    private void initializing(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_user_showing_achievement_header, this, true);

        tv_had_got_achievement = (TextView) findViewById(R.id.tv_had_got_achievement);
        layout_showing_achievement = (ViewGroup) findViewById(R.id.layout_showing_achievement);

        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_right = (TextView) findViewById(R.id.tv_right);

        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    /**
     * @param isShow 显示已展示的成就
     */
    public void setShowingLayoutVisibile(boolean isShow) {
        if (isShow) {
            tv_had_got_achievement.setVisibility(VISIBLE);
            layout_showing_achievement.setVisibility(VISIBLE);
        } else {
            tv_had_got_achievement.setVisibility(GONE);
            layout_showing_achievement.setVisibility(GONE);
        }
    }

    public void setOnTabChangeListener(ITabCheck tabCheck) {
        this.tabCheck = tabCheck;
    }

    public void startFirstCheck() {
        onClick(tv_left);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        if (tabCheck == null)
            return;

        final Resources res = getResources();
        final int colorNormal = res.getColor(R.color.gold);
        final int colorCheck = res.getColor(R.color.c_3a3a3a);

        tv_left.setTextColor(colorNormal);
        tv_left.setTextColor(colorNormal);

        tv_left.setSelected(false);
        tv_right.setSelected(false);

        if (v == tv_left) {
            tv_left.setTextColor(colorCheck);
            tv_left.setSelected(true);
            tabCheck.onLeftCheck();
        }
        if (v == tv_right) {
            tv_right.setTextColor(colorCheck);
            tv_right.setSelected(true);
            tabCheck.onRightCheck();
        }
    }
}
