package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tysci.ballq.R;

/**
 * Created by HTT on 2016/6/13.
 */
public class BallQHomePageLayout extends LinearLayout{
    public BallQHomePageLayout(Context context) {
        super(context);
        initViews(context);
    }

    public BallQHomePageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public BallQHomePageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BallQHomePageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.fragment_ballq_home_page,this,true);

    }
}
