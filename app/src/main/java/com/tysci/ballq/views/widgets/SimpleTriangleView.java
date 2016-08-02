package com.tysci.ballq.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.tysci.ballq.R;

/**
 * Created by Administrator on 2016-08-02 0002.
 */
public class SimpleTriangleView extends View
{
    public final static int DEFAULT_TRIANGLE_COLOR = Color.YELLOW;

    private Path mPath;
    private Paint mPaint;

    private int mTriangleColor = DEFAULT_TRIANGLE_COLOR;

    public SimpleTriangleView(Context context)
    {
        this(context, null);
    }

    public SimpleTriangleView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SimpleTriangleView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleTriangleView, defStyle, 0);
        mTriangleColor = a.getColor(R.styleable.SimpleTriangleView_triangle_color, DEFAULT_TRIANGLE_COLOR);

        a.recycle();
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        final int width = getWidth();
        final int height = getHeight();

        mPath.moveTo(0, 0);
        mPath.lineTo(width / 2, height);
        mPath.lineTo(width, 0);
        mPath.close();

        mPaint.setColor(mTriangleColor);

        canvas.drawPath(mPath, mPaint);
    }
}
