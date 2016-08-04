package com.tysci.ballq.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.interfaces.ITabCheck;
import com.tysci.ballq.utils.ImageUtil;

import java.util.List;

/**
 * Created by Administrator on 2016-07-15 0015.
 *
 * @author LinDe
 */
public final class UserAchievementHeaderView extends LinearLayout implements View.OnClickListener
{
    private ViewGroup layout_had_got_achievement;// 已选择展示的成就提示
    private ViewGroup layout_showing_achievement;// 展示的成就

    private ImageView user_showing_achievement_1, user_showing_achievement_2;

    private TextView tv_left;
    private TextView tv_right;

    private ITabCheck tabCheck;

    private TextView text_achievement_msg;// 他人成就是提示信息需要改动

    public UserAchievementHeaderView(Context context)
    {
        this(context, null);
    }

    public UserAchievementHeaderView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public UserAchievementHeaderView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializing(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UserAchievementHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializing(context);
    }

    private void initializing(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_user_showing_achievement_header, this, true);

        layout_had_got_achievement = (ViewGroup) findViewById(R.id.layout_had_got_achievement);
        layout_showing_achievement = (ViewGroup) findViewById(R.id.layout_showing_achievement);

        user_showing_achievement_1 = (ImageView) findViewById(R.id.user_showing_achievement_1);
        user_showing_achievement_2 = (ImageView) findViewById(R.id.user_showing_achievement_2);

        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_right = (TextView) findViewById(R.id.tv_right);

        text_achievement_msg = (TextView) this.findViewById(R.id.text_achievement_msg);

        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    /**
     * @param isShow 显示已展示的成就
     */
    public void setShowingLayoutVisibility(boolean isShow)
    {
        if (isShow)
        {
            layout_had_got_achievement.setVisibility(VISIBLE);
            layout_showing_achievement.setVisibility(VISIBLE);
            text_achievement_msg.setText("请选择需要展示的成就:");
        }
        else
        {
            layout_had_got_achievement.setVisibility(GONE);
            layout_showing_achievement.setVisibility(GONE);
            text_achievement_msg.setText("成就获取情况:");
        }
    }

    public void setShowingAchievement(List<Integer> showingList)
    {
        int showing;

        user_showing_achievement_1.setImageResource(R.mipmap.icon_user_achievement_circle_mark);
        user_showing_achievement_2.setImageResource(R.mipmap.icon_user_achievement_circle_mark);

        showing = 0;
        try
        {
            showing = showingList.get(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setShowingAchievement(user_showing_achievement_1, showing);

        showing = 0;
        try
        {
            showing = showingList.get(1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setShowingAchievement(user_showing_achievement_2, showing);
    }

    private void setShowingAchievement(ImageView iv, int id)
    {
        if (id <= 0)
        {
            return;
        }
        String s = "achievement/" + id + ".png";
        ImageUtil.loadImage(iv, R.mipmap.icon_user_achievement_circle_mark, s);
    }

    public void setShowingListener(View.OnClickListener listener)
    {
        user_showing_achievement_1.setOnClickListener(listener);
        user_showing_achievement_2.setOnClickListener(listener);
    }

    public void setOnTabChangeListener(ITabCheck tabCheck)
    {
        this.tabCheck = tabCheck;
    }

    public void startFirstCheck()
    {
        onClick(tv_left);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v)
    {
        if (tabCheck == null)
            return;

        final Resources res = getResources();
        final int colorNormal = res.getColor(R.color.gold);
        final int colorCheck = res.getColor(R.color.c_3a3a3a);

        tv_left.setTextColor(colorNormal);
        tv_left.setTextColor(colorNormal);

        tv_left.setSelected(false);
        tv_right.setSelected(false);

        if (v == tv_left)
        {
            tv_left.setTextColor(colorCheck);
            tv_left.setSelected(true);
            tabCheck.onLeftCheck();
        }
        if (v == tv_right)
        {
            tv_right.setTextColor(colorCheck);
            tv_right.setSelected(true);
            tabCheck.onRightCheck();
        }
    }
}
