package com.tysci.ballq.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tysci.ballq.utils.KLog;

/**
 * Created by LinDe on 2016-08-09 0009.
 *
 * @see TextView 跑马灯
 */
public class MarqueeTextView extends TextView implements View.OnFocusChangeListener
{
    public MarqueeTextView(Context context)
    {
        this(context, null);
    }

    public MarqueeTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setOnFocusChangeListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnFocusChangeListener(this);
    }

    @Override
    public boolean isFocused()
    {
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (v == MarqueeTextView.this && !hasFocus)
        {
            KLog.d("requestFocus");
            requestFocus();
        }
    }
}
