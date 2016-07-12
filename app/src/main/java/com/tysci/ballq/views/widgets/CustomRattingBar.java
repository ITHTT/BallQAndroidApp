package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tysci.ballq.R;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;

import java.util.List;

/**
 * Created by HTT on 2016/6/12.
 */
public class CustomRattingBar extends LinearLayout {
    /**星星数*/
    private int rattingBarCount=5;
    private int rattingBarWidth;
    private int rattingBarHeight;
    private int rattingBarPadding;
    private List<ImageView> rattingBarItemViews;
    private int rattingValue;

    public CustomRattingBar(Context context) {
        super(context);
        initViews(context,null);
    }

    public CustomRattingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    public CustomRattingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomRattingBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context,attrs);
    }

    private void initViews(Context context,AttributeSet attrs){
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        rattingBarWidth= CommonUtils.dip2px(context,15);
        rattingBarHeight= CommonUtils.dip2px(context,15);
        rattingBarPadding= CommonUtils.dip2px(context,2);

        if(attrs!=null){
            TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CustomRattingBar);
            rattingBarWidth=typedArray.getDimensionPixelSize(R.styleable.CustomRattingBar_rattingBar_width, CommonUtils.dip2px(context,15));
            rattingBarHeight=typedArray.getDimensionPixelSize(R.styleable.CustomRattingBar_rattingBar_height, CommonUtils.dip2px(context,15));
            rattingBarCount=typedArray.getInt(R.styleable.CustomRattingBar_rattingBar_num,5);
            rattingBarPadding=typedArray.getDimensionPixelSize(R.styleable.CustomRattingBar_rattingBar_padding, CommonUtils.dip2px(context,2));
            typedArray.recycle();
        }
        //rattingBarItemViews=new ArrayList<>(rattingBarCount);
        KLog.e("RattingBarNum:"+rattingBarCount);
        KLog.e("width:"+rattingBarWidth);
        KLog.e("height:"+rattingBarHeight);
        for(int i=0;i<rattingBarCount;i++){
            addRattingBarItem(context,i);
        }
    }

    private void addRattingBarItem(Context context,int i){
        ImageView imageView=new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.drawable.icon_custom_ratting_bar_selector);
        LayoutParams layoutParams=new LayoutParams(rattingBarWidth,rattingBarHeight);
        if(i!=0){
            layoutParams.setMargins(rattingBarPadding,0,0,0);
        }
        imageView.setLayoutParams(layoutParams);
        addView(imageView);
    }

    public void setRattingValue(int value){
        this.rattingValue=value;
        for(int i=0;i<value&&i<rattingBarCount;i++){
            ImageView child= (ImageView) getChildAt(i);
            child.setSelected(true);
        }
    }



}
