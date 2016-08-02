package com.tysci.ballq.views.widgets;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/5/20.
 */
public class BqArticleCoverImageView extends SelectableRoundedImageView{
    public BqArticleCoverImageView(Context context) {
        super(context);
    }

    public BqArticleCoverImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BqArticleCoverImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=width*9/16;
        setMeasuredDimension(width, height);
    }
}
