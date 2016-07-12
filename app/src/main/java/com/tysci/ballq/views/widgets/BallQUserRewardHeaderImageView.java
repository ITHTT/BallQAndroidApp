package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by HTT on 2016/6/6.
 */
public class BallQUserRewardHeaderImageView extends ImageView{
    public BallQUserRewardHeaderImageView(Context context) {
        super(context);
    }

    public BallQUserRewardHeaderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BallQUserRewardHeaderImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BallQUserRewardHeaderImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=width;
        setMeasuredDimension(width, height);
    }
}
