package com.tysci.ballq.views.widgets.chartview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.tysci.ballq.utils.CommonUtils;

import java.util.Locale;

/**
 * Created by HTT on 2016/6/10.
 */
public class MatchBettingScaleHorizontalLineView extends View {
    private float winScale;
    private float equalScale;
    private float loseScale;
    private float all;

    private int winColor= Color.parseColor("#d3bd6e");
    private int equalColor=Color.parseColor("#b6a25e");
    private int loseColor=Color.parseColor("#7d7248");
    private int percentColor=Color.parseColor("#3a3a3a");

    private Paint paint=null;

    private int width;
    private int height;
    /**线条宽度*/
    private int lineHeight;
    private int padding;
    private int textHeght;
    private boolean isBigSmall=false;


    private Rect rect=null;

    public MatchBettingScaleHorizontalLineView(Context context) {
        super(context);
        initViews(context);
    }

    public MatchBettingScaleHorizontalLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public MatchBettingScaleHorizontalLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MatchBettingScaleHorizontalLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        lineHeight= CommonUtils.dip2px(context, 10);
        padding= CommonUtils.dip2px(context, 5);
        textHeght= CommonUtils.dip2px(context, 15);

        rect=new Rect();
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        if(height<lineHeight+2*(padding+textHeght)){
            height=lineHeight+2*(padding+textHeght)+20;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w!=oldw||h!=oldh){
            width=w;
            height=h;
        }
    }


    public void setBettingScaleData(float win,float equal,float lose){
        this.winScale=win;
        this.equalScale=equal;
        this.loseScale=lose;
        this.all=win+equal+lose;
        postInvalidate();
    }

    public void setIsBigSmall(boolean isBigSmall) {
        this.isBigSmall = isBigSmall;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLineInfo(canvas);
    }

    private void drawLineInfo(Canvas canvas){
        paint.setStrokeWidth(0.5f);
        paint.setTextSize(CommonUtils.sp2px(getContext(), 13));
        int winWidth= (int) ((this.winScale/this.all)*width);
        if(winWidth>0){
            paint.setColor(winColor);
            rect.left=0;
            rect.top=height/2-lineHeight/2;
            rect.right=winWidth;
            rect.bottom=height/2+lineHeight/2;
            canvas.drawRect(rect,paint);

            /**绘制数值*/
            String winScaleText=(isBigSmall?"大球 ":"胜 ")+String.format(Locale.getDefault(),"%.1f",winScale);
            float textWidth=paint.measureText(winScaleText, 0, winScaleText.length());
            float textX=winWidth/2-textWidth/2;
            if(textX<0){
                textX=winWidth/2;
            }
            canvas.drawText(winScaleText,textX,height/2-lineHeight-padding+textHeght/2-10,paint);

            /**绘制百分比*/
            paint.setColor(percentColor);
            String winPercentText=String.format(Locale.getDefault(),"%.0f",winScale/all*100)+"%";
            textWidth=paint.measureText(winPercentText, 0, winPercentText.length());
            textX=winWidth/2-textWidth/2;
            if(textX<0){
                textX=winWidth/2;
            }
            canvas.drawText(winPercentText,textX,height/2+lineHeight+padding+15,paint);
        }

        int equalWidth=winWidth+(int) ((this.equalScale/this.all)*width);
        if(equalWidth>winWidth){
            paint.setColor(equalColor);
            rect.left=winWidth;
            rect.top=height/2-lineHeight/2;
            rect.right=equalWidth;
            rect.bottom=height/2+lineHeight/2;
            canvas.drawRect(rect,paint);

            /**绘制数值*/
            String equalScaleText="平 "+String.format(Locale.getDefault(),"%.1f",equalScale);
            float textWidth=paint.measureText(equalScaleText, 0, equalScaleText.length());
            float textX=winWidth+(equalWidth-winWidth)/2-textWidth/2;
            if(textX<0){
                textX=winWidth+equalWidth/2;
            }else if(winWidth+textWidth>width){
                textX=width-textWidth;
            }
            canvas.drawText(equalScaleText,textX,height/2-lineHeight-padding+textHeght/2-10,paint);

            /**绘制百分比*/
            paint.setColor(percentColor);
            String equalPercentText=String.format(Locale.getDefault(),"%.0f",equalScale/all*100)+"%";
            textWidth=paint.measureText(equalPercentText, 0, equalPercentText.length());
            textX=winWidth+(equalWidth-winWidth)/2-textWidth/2;
            if(textX<0){
                textX=winWidth+equalWidth/2;
            }else if(winWidth+textWidth>width){
                textX=width-textWidth;
            }
            canvas.drawText(equalPercentText,textX,height/2+lineHeight+padding+15,paint);
        }

        int loseWidth=width-equalWidth;
        if(loseWidth>0){
            paint.setColor(loseColor);
            rect.left=equalWidth;
            rect.top=height/2-lineHeight/2;
            rect.right=width;
            rect.bottom=height/2+lineHeight/2;
            canvas.drawRect(rect,paint);

            /**绘制数值*/
            String loseScaleText=(isBigSmall?"小球 ":"负 ")+String.format(Locale.getDefault(),"%.1f",loseScale);
            float textWidth=paint.measureText(loseScaleText, 0, loseScaleText.length());
            float textX=equalWidth+loseWidth/2-textWidth/2;
            if(textX<0){
                textX=equalWidth+loseWidth/2;
            }else if(equalWidth+textWidth>width){
                textX=width-textWidth;
            }
            canvas.drawText(loseScaleText,textX,height/2-lineHeight-padding+textHeght/2-10,paint);

            /**绘制百分比*/
            paint.setColor(percentColor);
            String losePercentText=String.format(Locale.getDefault(),"%.0f",loseScale/all*100)+"%";
            textWidth=paint.measureText(losePercentText, 0, losePercentText.length());
            textX=equalWidth+loseWidth/2-textWidth/2;
            if(textX<0){
                textX=equalWidth+loseWidth/2;
            }else if(equalWidth+textWidth>width){
                textX=width-textWidth;
            }
            canvas.drawText(losePercentText,textX,height/2+lineHeight+padding+15,paint);
        }
    }
}
