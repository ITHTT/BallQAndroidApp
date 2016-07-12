package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HTT on 2016/6/8.
 * 圆形进度条
 */
public class CircleProgressView extends View {
    private final int MAX_VALUE=100;
    private int progressValue=0;
    /**圆环的宽度*/
    private int circleStrokeWidth=10;
    private int textStrokeWidth=5;

    private int circleColor= Color.parseColor("#d4d4d4");
    private int progressColor=Color.parseColor("#ff0000");
    private int textColor=Color.parseColor("#a4a4a8");
    /**标题*/
    private String titleLable="盈利率";
    /**内容*/
    private String contextLable;

    private int radius;

    private Paint mPaint;

    private int currentProgress=0;

    private PaintFlagsDrawFilter mDrawFilter;

    // 画圆所在的距形区域
    private  RectF cirlceRectF;


    public CircleProgressView(Context context) {
        super(context);
        initViews(context);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        mPaint=new Paint();
        cirlceRectF=new RectF();
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    public void setTitleLable(String titleLable) {
        this.titleLable = titleLable;
    }

    public void setProgressValue(int progress){
        currentProgress=0;
        this.progressValue=progress;
        this.contextLable=progress+"%";
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius=Math.min(w,h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mDrawFilter);
        int value=Math.abs(progressValue);
        if(currentProgress<=value) {
            if(progressValue<0) {
                contextLable = "-"+value + "%";
            }else{
                contextLable=value+"%";
            }
            drawCircle(canvas);
            drawTextLabel(canvas);
            if(currentProgress==value){
                return;
            }
            currentProgress+=2;
            if(currentProgress<value){
                postInvalidate();
            }else{
                currentProgress=value;
                postInvalidate();
            }
        }
    }

    /**绘制背景圆*/
    private void drawCircle(Canvas canvas){
        mPaint.setAntiAlias(true);
        mPaint.setColor(circleColor);
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(circleStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        // 位置
        cirlceRectF.left = circleStrokeWidth/ 2; // 左上角x
        cirlceRectF.top = circleStrokeWidth / 2; // 左上角y
        cirlceRectF.right = radius - circleStrokeWidth / 2; // 左下角x
        cirlceRectF.bottom = radius - circleStrokeWidth / 2; // 右下角y

        // 绘制圆圈，进度条背景
        canvas.drawArc(cirlceRectF, -90, 360, false, mPaint);
        mPaint.setColor(progressColor);
        if(progressValue<0) {
            canvas.drawArc(cirlceRectF, -90, 0, false, mPaint);
        }else{
            canvas.drawArc(cirlceRectF, -90, ((float) currentProgress / MAX_VALUE) * 360, false, mPaint);
        }
    }

    private void drawTextLabel(Canvas canvas){
        mPaint.setStrokeWidth(textStrokeWidth+2);
        mPaint.setTypeface(Typeface.MONOSPACE);
        mPaint.setColor(textColor);
        int textHeight = radius / 4;
        mPaint.setTextSize(textHeight);
        int textWidth = (int) mPaint.measureText(contextLable, 0, contextLable.length());
        mPaint.setStyle(Paint.Style.FILL);
        Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
        //int fontHeight= (int) (fontMetrics.bottom-fontMetrics.top);
        canvas.drawText(contextLable, radius / 2 - textWidth / 2, radius / 2+(fontMetrics.bottom-fontMetrics.top)/ 2+5, mPaint);

        textHeight = radius / 6;
        mPaint.setTextSize(textHeight);
        fontMetrics=mPaint.getFontMetrics();
        textWidth = (int) mPaint.measureText(titleLable, 0, titleLable.length());
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(titleLable, radius / 2 - textWidth / 2, radius / 2 - (fontMetrics.bottom-fontMetrics.top)/ 2, mPaint);
    }

}