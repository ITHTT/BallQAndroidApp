package com.tysci.ballq.views.widgets.chartview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.tysci.ballq.utils.CommonUtils;

import java.util.Locale;

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

    private int lineColor= Color.parseColor("#eacb70");
    private int innerCircleColor=Color.parseColor("#ffffff");
    private int percentTextColor=Color.parseColor("#3a3a3a");

    private int winColor= Color.parseColor("#d3bd6e");
    private int equalColor=Color.parseColor("#b6a25e");
    private int loseColor=Color.parseColor("#7d7248");

    private float winValue;
    private float equalValue;
    private float loseValue;
    private Context context;
    private Paint paint=null;
    private boolean isBigSmall=false;

    private int width;
    private int height;

    public MatchBettingScaleLineView(Context context) {
        super(context);
        initViews(context);
    }

    public MatchBettingScaleLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public MatchBettingScaleLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MatchBettingScaleLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context=context;
        bettingInfoTextHeight= CommonUtils.dip2px(context, 20);
        bettingPercentTextHeight= CommonUtils.dip2px(context, 25);
        LEFT_RIGHT_MARGIN= CommonUtils.dip2px(context, 15);
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(CommonUtils.px2sp(context, 13));
    }

    public void setIsBigSmall(boolean isBigSmall) {
        this.isBigSmall = isBigSmall;
    }

    public void setBettingDatas(float win,float equal,float lose){
        this.winValue=win;
        this.equalValue=equal;
        this.loseValue=lose;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w!=oldw||h!=oldh){
            width=w;
            height=h;
            lineAreaHeight=height-bettingPercentTextHeight-bettingInfoTextHeight-2*OUTER_CIRCLE_WIDTH;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float allValue=winValue+loseValue+equalValue;
        if(allValue<=0f){
            return;
        }

        float winPercent=winValue/allValue;
        float equalPercent=equalValue/allValue;
        float losePercent=loseValue/allValue;

        float winX=LEFT_RIGHT_MARGIN;
        float equalX=width/2;
        float loseX=width-LEFT_RIGHT_MARGIN;

        float winY=bettingInfoTextHeight+2*OUTER_CIRCLE_WIDTH+lineAreaHeight*(1f-winPercent);
        float equalY=bettingInfoTextHeight+2*OUTER_CIRCLE_WIDTH+lineAreaHeight*(1f-equalPercent);
        float loseY=bettingInfoTextHeight+2*OUTER_CIRCLE_WIDTH+lineAreaHeight*(1f-losePercent);

        float textInfoY= CommonUtils.dip2px(context, 20);
        float percentTextPadding=60;
        if(equalPercent<=0f){

            paint.setColor(lineColor);
            paint.setStrokeWidth(LINE_WIDTH);
            /**画线条*/
            canvas.drawLine(winX, winY, loseX, loseY, paint);
            /**画外圆*/
            canvas.drawCircle(winX,winY,OUTER_CIRCLE_WIDTH,paint);
            canvas.drawCircle(loseX, loseY, OUTER_CIRCLE_WIDTH, paint);
            /**画内圆*/
            paint.setColor(innerCircleColor);
            canvas.drawCircle(winX, winY, INNER_CIRCLE_WIDTH, paint);
            canvas.drawCircle(loseX, loseY, INNER_CIRCLE_WIDTH, paint);

            /**绘制信息*/
            paint.setTextSize(CommonUtils.sp2px(context, 13));
            paint.setStrokeWidth(0.5f);
            paint.setColor(winColor);
            String bettingWinInfo=(isBigSmall?"大球 ":"胜 ")+String.format(Locale.getDefault(), "%.0f", winValue);
            float winTextWidth=paint.measureText(bettingWinInfo, 0, bettingWinInfo.length());
            if(winX-winTextWidth/2<0){
                canvas.drawText(bettingWinInfo, 0, textInfoY, paint);
            }else{
                canvas.drawText(bettingWinInfo, winX - winTextWidth / 2, textInfoY, paint);
            }

            paint.setColor(loseColor);
            String bettingLoseInfo=(isBigSmall?"小球 ":"负 ")+String.format(Locale.getDefault(), "%.0f", loseValue);
            float loseTextWidth=paint.measureText(bettingLoseInfo,0,bettingLoseInfo.length());
            if(loseX+loseTextWidth/2>width){
                canvas.drawText(bettingLoseInfo,width-loseTextWidth,textInfoY,paint);
            }else{
                canvas.drawText(bettingLoseInfo,loseX-loseTextWidth/2,textInfoY,paint);
            }

            /**绘制百分比信息*/
            paint.setColor(percentTextColor);
            String winPercentStr=String.format(Locale.getDefault(),"%.0f",winPercent*100)+"%";
            float winPercentTextWidth=paint.measureText(winPercentStr,0,winPercentStr.length());
            if(winX-winPercentTextWidth/2<0){
                canvas.drawText(winPercentStr,0,winY+percentTextPadding,paint);
            }else{
                canvas.drawText(winPercentStr,winX-winPercentTextWidth/2,winY+percentTextPadding,paint);
            }

            String losePercentStr=String.format(Locale.getDefault(),"%.0f",losePercent*100)+"%";
            float losePercentTextWidth=paint.measureText(losePercentStr,0,losePercentStr.length());
            if(loseX+losePercentTextWidth/2>width){
                canvas.drawText(losePercentStr,width-losePercentTextWidth,loseY+percentTextPadding,paint);
            }else{
                canvas.drawText(losePercentStr,loseX-losePercentTextWidth/2,loseY+percentTextPadding,paint);
            }
        }else if(winPercent<=0f){
            paint.setColor(lineColor);
            paint.setStrokeWidth(LINE_WIDTH);
            /**画线条*/
            canvas.drawLine(equalX, equalY, loseX, loseY, paint);
            /**画外圆*/
            canvas.drawCircle(equalX,equalY,OUTER_CIRCLE_WIDTH,paint);
            canvas.drawCircle(loseX, loseY, OUTER_CIRCLE_WIDTH, paint);
            /**画内圆*/
            paint.setColor(innerCircleColor);
            canvas.drawCircle(equalX, equalY, INNER_CIRCLE_WIDTH, paint);
            canvas.drawCircle(loseX, loseY, INNER_CIRCLE_WIDTH, paint);


            paint.setTextSize(CommonUtils.sp2px(context, 13));
            paint.setStrokeWidth(0.5f);
            /**绘制信息*/
            paint.setColor(equalColor);
            String bettingEqualInfo="平 "+String.format(Locale.getDefault(), "%.0f", equalValue);
            float equalTextWidth=paint.measureText(bettingEqualInfo, 0, bettingEqualInfo.length());
            canvas.drawText(bettingEqualInfo, equalX - equalTextWidth / 2, textInfoY, paint);
            paint.setColor(loseColor);
            String bettingLoseInfo=(isBigSmall?"小球 ":"负 ")+String.format(Locale.getDefault(), "%.0f", loseValue);
            float loseTextWidth=paint.measureText(bettingLoseInfo,0,bettingLoseInfo.length());
            if(loseX+loseTextWidth/2>width){
                canvas.drawText(bettingLoseInfo,width-loseTextWidth,textInfoY,paint);
            }else{
                canvas.drawText(bettingLoseInfo,loseX-loseTextWidth/2,textInfoY,paint);
            }

            /**绘制百分比信息*/
            paint.setColor(percentTextColor);
            String equalPercentStr=String.format(Locale.getDefault(),"%.0f",equalPercent*100)+"%";
            float equalPercentTextWidth=paint.measureText(equalPercentStr,0,equalPercentStr.length());
            canvas.drawText(equalPercentStr,equalX-equalPercentTextWidth/2,equalY+OUTER_CIRCLE_WIDTH/2+percentTextPadding,paint);
            String losePercentStr=String.format(Locale.getDefault(),"%.0f",losePercent*100)+"%";
            float losePercentTextWidth=paint.measureText(losePercentStr,0,losePercentStr.length());
            canvas.drawText(losePercentStr,loseX-losePercentTextWidth/2,equalY+OUTER_CIRCLE_WIDTH/2+percentTextPadding,paint);

        }else if(losePercent<=0f){

        }else{
            /**画线条*/
            paint.setColor(lineColor);
            paint.setStrokeWidth(LINE_WIDTH);
            canvas.drawLine(winX, winY, equalX, equalY, paint);
            canvas.drawLine(equalX, equalY, loseX, loseY, paint);
            /**画外圆*/
            canvas.drawCircle(winX, winY, OUTER_CIRCLE_WIDTH, paint);
            canvas.drawCircle(equalX, equalY, OUTER_CIRCLE_WIDTH, paint);
            canvas.drawCircle(loseX, loseY, OUTER_CIRCLE_WIDTH, paint);
            /**画内圆*/
            paint.setColor(innerCircleColor);
            canvas.drawCircle(winX, winY, INNER_CIRCLE_WIDTH, paint);
            canvas.drawCircle(equalX, equalY, INNER_CIRCLE_WIDTH, paint);
            canvas.drawCircle(loseX, loseY, INNER_CIRCLE_WIDTH, paint);

            /**绘制信息*/
            paint.setTextSize(CommonUtils.sp2px(context, 13));
            paint.setStrokeWidth(0.5f);
            paint.setColor(winColor);
            String bettingWinInfo=(isBigSmall?"大球 ":"胜 ")+String.format(Locale.getDefault(), "%.0f", winValue);
            float winTextWidth=paint.measureText(bettingWinInfo, 0, bettingWinInfo.length());
            if(winX-winTextWidth/2<0){
                canvas.drawText(bettingWinInfo, 0, textInfoY, paint);
            }else{
                canvas.drawText(bettingWinInfo, winX - winTextWidth / 2, textInfoY, paint);
            }

            paint.setColor(equalColor);
            String bettingEqualInfo="平 "+String.format(Locale.getDefault(), "%.0f", equalValue);
            float equalTextWidth=paint.measureText(bettingEqualInfo, 0, bettingEqualInfo.length());
            canvas.drawText(bettingEqualInfo, equalX - equalTextWidth / 2, textInfoY, paint);

            paint.setColor(loseColor);
            String bettingLoseInfo=(isBigSmall?"小球 ":"负 ")+String.format(Locale.getDefault(), "%.0f", loseValue);
            float loseTextWidth=paint.measureText(bettingLoseInfo,0,bettingLoseInfo.length());
            if(loseX+loseTextWidth/2>width){
                canvas.drawText(bettingLoseInfo,width-loseTextWidth,textInfoY,paint);
            }else{
                canvas.drawText(bettingLoseInfo,loseX-loseTextWidth/2,textInfoY,paint);
            }

            /**绘制百分比信息*/
            paint.setColor(percentTextColor);
            String winPercentStr=String.format(Locale.getDefault(),"%.0f",winPercent*100)+"%";
            float winPercentTextWidth=paint.measureText(winPercentStr,0,winPercentStr.length());
            if(winX-winPercentTextWidth/2<0){
                canvas.drawText(winPercentStr,0,winY+percentTextPadding,paint);
            }else{
                canvas.drawText(winPercentStr,winX-winPercentTextWidth/2,winY+percentTextPadding,paint);
            }

            String equalPercentStr=String.format(Locale.getDefault(),"%.0f",equalPercent*100)+"%";
            float equalPercentTextWidth=paint.measureText(equalPercentStr,0,equalPercentStr.length());
            canvas.drawText(equalPercentStr,equalX-equalPercentTextWidth/2,equalY+percentTextPadding,paint);

            String losePercentStr=String.format(Locale.getDefault(),"%.0f",losePercent*100)+"%";
            float losePercentTextWidth=paint.measureText(losePercentStr,0,losePercentStr.length());
            if(loseX+losePercentTextWidth/2>width){
                canvas.drawText(losePercentStr,width-losePercentTextWidth,loseY+percentTextPadding,paint);
            }else{
                canvas.drawText(losePercentStr,loseX-losePercentTextWidth/2,loseY+percentTextPadding,paint);
            }
        }
    }
}
