package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by HTT on 2016/5/30.
 */
public class TitleBar extends LinearLayout
{
    private ImageView ivBack;
    private RelativeLayout layoutLeftBack;

    private TextView tvTitle;
    private ImageView mTitleMore;

    private ImageView ivNextMenu01;
    private ImageView ivNextMenu02;
    private TextView tvNextMenu;

    public TitleBar(Context context)
    {
        super(context);
        initViews(context);
    }

    public TitleBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initViews(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar_content, this, true);
        ivBack = (ImageView) this.findViewById(R.id.iv_titlebar_left);
        layoutLeftBack = (RelativeLayout) this.findViewById(R.id.layout_titlebar_back);

        tvTitle = (TextView) this.findViewById(R.id.tv_titlebar_title);
        tvTitle.setFocusable(true);
        tvTitle.requestFocus();
        mTitleMore = (ImageView) this.findViewById(R.id.iv_title_more);

        ivNextMenu01 = (ImageView) this.findViewById(R.id.iv_titlebar_next_menu01);
        ivNextMenu02 = (ImageView) this.findViewById(R.id.iv_titlebar_next_menu02);
        tvNextMenu = (TextView) this.findViewById(R.id.tv_titlebar_next);

        mTitleMore.setVisibility(GONE);

        layoutLeftBack.setId(R.id.iv_titlebar_left);
    }

    public void setTitleMoreVisibility(int visibility)
    {
        mTitleMore.setVisibility(visibility);
    }

    public void setTitleBarLeftIcon(int res, OnClickListener onClickListener)
    {
        ivBack.setImageResource(res);
        layoutLeftBack.setOnClickListener(onClickListener);
    }

    public void setTitleBarTitle(String title)
    {
        tvTitle.setText(title);
    }

    public void setTitleClickListener(View.OnClickListener listener)
    {
        tvTitle.setOnClickListener(listener);
    }

    public void setTitleLongClickListener(View.OnLongClickListener longClickListener)
    {
        tvTitle.setOnLongClickListener(longClickListener);
    }

    public ImageView getLeftBack()
    {
        return ivBack;
    }

    public ViewGroup getLeftBackGroup()
    {
        return layoutLeftBack;
    }


    public void setTitleBarTitle(int titleRes)
    {
        tvTitle.setText(titleRes);
    }

    public TextView getTitleBarTitle()
    {
        return tvTitle;
    }

    public void resetTitle()
    {
        tvTitle.setBackgroundResource(0);
        tvTitle.setCompoundDrawables(null, null, null, null);
        tvTitle.setTextSize(16f);
        tvTitle.setTextColor(this.getResources().getColor(R.color.gold));
        tvTitle.setOnClickListener(null);
    }

    public void setRightMenuIcon(int res, OnClickListener onClickListener)
    {
        ivNextMenu01.setVisibility(View.VISIBLE);
        ivNextMenu01.setImageResource(res);
        ivNextMenu01.setOnClickListener(onClickListener);
    }

    public ImageView getRightMenuImageView()
    {
        ivNextMenu01.setVisibility(View.VISIBLE);
        return ivNextMenu01;
    }

    public void setSecondRightMenuIcon(int res, OnClickListener onClickListener)
    {
        ivNextMenu02.setVisibility(View.VISIBLE);
        ivNextMenu02.setImageResource(res);
        ivNextMenu02.setOnClickListener(onClickListener);
    }

    public void setRightMenuText(String title, OnClickListener onClickListener)
    {
        tvNextMenu.setVisibility(View.VISIBLE);
        tvNextMenu.setText(title);
        tvNextMenu.setOnClickListener(onClickListener);
    }

    public TextView getRightMenuTextView()
    {
        tvNextMenu.setVisibility(View.VISIBLE);
        return tvNextMenu;
    }

    public void setRightOnClickListener(OnClickListener listener)
    {
        this.findViewById(R.id.layout_titlebar_next).setOnClickListener(listener);
    }


}
