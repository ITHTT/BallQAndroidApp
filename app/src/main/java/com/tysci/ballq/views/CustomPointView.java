package com.tysci.ballq.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tysci.ballq.R;

/**
 * Created by LinDe on 2016-07-01.
 * CustomPointView
 */
public class CustomPointView extends LinearLayout {
    private int bgNormal;
    private int bgCheck;
    private int imageLength;
    private int imageMargin;

    private int checkPosition;

    public CustomPointView(Context context) {
        super(context);
        init(context);
    }

    public CustomPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomPointView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, Object... objects) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(Color.parseColor("#00000000"));

        AttributeSet attrs = null;
        try {
            attrs = (AttributeSet) objects[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        bgNormal = R.mipmap.ic_launcher;
        bgCheck = R.mipmap.ic_launcher;
        checkPosition = 0;
        int pointNumber = 0;

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomPointView);

            bgNormal = ta.getResourceId(R.styleable.CustomPointView_point_image_normal, bgNormal);
            bgCheck = ta.getResourceId(R.styleable.CustomPointView_point_image_check, bgCheck);
            pointNumber = ta.getInteger(R.styleable.CustomPointView_point_image_size, checkPosition);
            imageLength = (int) ta.getDimension(R.styleable.CustomPointView_point_image_width_height, 0);
            imageMargin = (int) ta.getDimension(R.styleable.CustomPointView_point_image_margin, 0);

            ta.recycle();
        }
        setPointNumber(pointNumber);
    }

    public void setCheckPosition(int position) {
        if (position < 0 || position >= getChildCount()) {
            return;
        }
        checkPosition = position;
        refreshBackground();
    }

    private void refreshBackground() {
        ImageView iv;
        for (int i = 0, length = getChildCount(); i < length; i++) {
            iv = (ImageView) getChildAt(i);
            iv.setImageResource(i == checkPosition ? bgCheck : bgNormal);
        }
    }

    public void setPointNumber(int number) {
        removeAllViews();

        final Context context = getContext();
        ImageView iv;
        for (int i = 0; i < number; i++) {
            iv = new ImageView(context);
            addView(iv);
            LayoutParams lp = (LayoutParams) iv.getLayoutParams();
            lp.width = imageLength;
            lp.height = imageLength;
            if (i > 0) {
                lp.leftMargin = imageMargin;
            }
        }
        if (number > 0) {
            checkPosition = 0;
            refreshBackground();
            invalidate();
        }
        setCheckPosition(0);
    }

}
