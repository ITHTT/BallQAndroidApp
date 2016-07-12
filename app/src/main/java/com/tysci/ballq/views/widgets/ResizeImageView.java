package com.tysci.ballq.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/5/20.
 */
public class ResizeImageView extends SelectableRoundedImageView{
    public ResizeImageView(Context context) {
        super(context);
    }

    public ResizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width= View.MeasureSpec.getSize(widthMeasureSpec);
        int height=width*9/16;
        setMeasuredDimension(width, height);
    }
}
