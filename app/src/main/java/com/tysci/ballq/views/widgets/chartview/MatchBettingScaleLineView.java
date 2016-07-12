package com.tysci.ballq.views.widgets.chartview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HTT on 2016/6/11.
 */
public class MatchBettingScaleLineView extends View{
    public static final int LINE_WIDTH = 5;// 线宽
    /**外圆宽度*/
    public static final int OUTER_CIRCLE_WIDTH = 20;
    /**内圆宽度*/
    public static final int INNER_CIRCLE_WIDTH = 10;
    private int LEFT_RIGHT_MARGIN;
    /**折线区域的高度*/
    private int lineAreaHeight;
    /**投注信息的文字高度*/
    private int bettingInfoTextHeight;
    /**投注百分比文字高度*/
    private int bettingPercentTextHeight;

    private String bettingWinInfo;
    private String bettingEqualInfo;
    private String bettingLoseInfo;

    private float winValue;
    private float equalValue;
    private float loseValue;

    public MatchBettingScaleLineView(Context context) {
        super(context);
    }

    public MatchBettingScaleLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatchBettingScaleLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MatchBettingScaleLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
