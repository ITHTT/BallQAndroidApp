package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.tysci.ballq.R;

/**
 * Created by HTT on 2016/6/4.
 */
public class TriangleView extends View {
    private final int DEFAULT_COLOR=Color.parseColor("#eacb70");
    private final int DEFAULT_SHADOW_COLOR=Color.parseColor("#dedfe4");
    /**画笔*/
    private Paint paint=null;
    /**宽度*/
    private float width;
    /**高度*/
    private float height;

    private Path path=null;

    private int backgroundColor= DEFAULT_COLOR;
    private int shadowColor=DEFAULT_SHADOW_COLOR;
    private int shadowRadius=0;
    private int shadowDx=0;
    private int shadowDy=0;


    public TriangleView(Context context) {
        super(context);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initViews(Context context,AttributeSet attrs){
        if(attrs!=null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TriangleView);
            backgroundColor=ta.getColor(R.styleable.TriangleView_background_color,DEFAULT_COLOR);
            shadowColor=ta.getColor(R.styleable.TriangleView_shadow_color,DEFAULT_SHADOW_COLOR);
            shadowRadius=ta.getDimensionPixelSize(R.styleable.TriangleView_shadow_radius,0);
            shadowDx=ta.getDimensionPixelSize(R.styleable.TriangleView_shadow_dx,0);
            shadowDy=ta.getDimensionPixelSize(R.styleable.TriangleView_shadow_dy,0);
            ta.recycle();
        }

        paint=new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w!=oldw||h!=oldh){
            width=w;
            height=h;
            path.moveTo(getLeft(),getTop());
            path.lineTo(getLeft()+getRight()/2,getBottom());
            path.lineTo(getRight(),getTop());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
