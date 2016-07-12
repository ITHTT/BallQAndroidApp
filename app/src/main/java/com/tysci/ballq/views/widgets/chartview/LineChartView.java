package com.tysci.ballq.views.widgets.chartview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by LinDe on 2016-03-22 0022.
 * 大数据折线图
 */
public class LineChartView extends View {
    private final static int PADDING_TOP = 25;
    private final static float MAIN_STROKE_WIDTH = 6;
    private final static float HISTORY_STROKE_WIDTH = 2;
    private final Paint paint1;

    private OnDrawListener onDrawListener=null;

    /**
     * Y坐标的最小值、最大值
     */
    private float firstY, endY;

    /**
     * 填充数据
     */
    private final List<XY> handicapList;
    /**
     * 主折线图数据
     */
    private final List<XY> mainList;
    /**
     * 历史折线图数据
     */
    private final List<List<XY>> historyList;
    private boolean isToShowHistoryList;
    /**
     * 需要显示的Y坐标值
     */
    private float yTop, yCenter, yBottom;
    /**
     * 当前比赛场次的开赛时间
     */
    private String resultDate;
    /**
     * 如果比赛未开始，距离开始的分钟数
     */
    private int differMinute;
    /**
     * 绘制盘口颜色
     */
    private boolean handicapColorCheck;
    /**
     * 当前绘制盘口位置
     */
    private int handicapPosition;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint1 = new Paint();
        paint1.setAntiAlias(true);
        mainList = new ArrayList<>();
        historyList = new ArrayList<>();
        handicapList = new ArrayList<>();
    }

    public void setOnDrawListener(OnDrawListener onDrawListener) {
        this.onDrawListener = onDrawListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 如果比赛未开始，初始化当前时间与比赛开始时间的时间差（分钟数）
         */
        try {
//            final Date date = DateFormatUtils.getStringToDate(resultDate);
            Date date= CommonUtils.getDateAndTimeFromGMT(resultDate);
//            final long resultTimeMillis = date == null ? 0 : date.getTime();
            if (date.getTime() > System.currentTimeMillis()) {
                final long millis = date.getTime() - System.currentTimeMillis();
                differMinute = (int) (millis / 1000 / 60);
            } else {
                differMinute = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        handicapPosition = 0;
        handicapColorCheck = true;
        onDrawing(canvas, getWidth(), getHeight());
    }


    protected void onDrawing(Canvas canvas, int width, int height) {
        final Resources res = getResources();
        final int w_num = width / 13;
        /**
         * 背景
         */
        paint1.setColor(res.getColor(R.color.white));
        canvas.drawRect(0, 0, width, height, paint1);
        /**
         * 刷新y的最低值和最高值
         */
        refreshY();
        if (firstY >= endY) {
            return;
        }
        paint1.setTypeface(Typeface.DEFAULT_BOLD);
        /**
         * 绘制盘口填充
         */
        drawHandicapList(canvas, width);
        /**
         * 绘制历史折线图
         */
        if (isToShowHistoryList) {
            drawHistoryList(canvas, width);
        }
        /**
         * 绘制主折线图
         */
        drawMainList(canvas, width);
        /**
         * 绘制虚线
         */
        try {
            final float lastStrokeWidth = paint1.getStrokeWidth();
            paint1.setStrokeWidth(2F);
            paint1.setColor(Color.parseColor("#959595"));
            drawDotLineHorizontal(canvas, width, 3, 0, getY(yCenter));
            drawDotLineHorizontal(canvas, width, 3, 0, getY(yTop));
            drawDotLineHorizontal(canvas, width, 3, 0, getY(yBottom));
            drawDotLineHorizontal(canvas, width, 3, 0, height - 1);
            paint1.setColor(Color.parseColor("#959595"));
            drawDotLineVertical(canvas, height, 15, w_num * 7, 0);
            paint1.setStrokeWidth(lastStrokeWidth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 盘口数据绘制
         */
        drawHandicapData(canvas, width);
    }

    private void drawHandicapData(Canvas canvas, int width) {
        paint1.setColor(Color.parseColor("#313131"));
        XY startXY = null;
        for (XY xy : mainList) {
            if (xy.x < 0 || xy.y < 0) {
                continue;
            }
            if (xy.x > 300) {
                startXY = xy;
                break;
            }
        }
        try {
            final int size = handicapList.size();
            // 5h显示一个数据
            XY xy5 = null;
            for (XY xy : handicapList) {
                if (xy.x <= 300)
                    break;
                if (xy5 == null) xy5 = xy;
                else if (xy5.x > xy.x) xy5 = xy;
            }
            if (xy5 != null)
                drawTextCenter(canvas, String.format(Locale.getDefault(), "%.2f", xy5.y), startXY != null ? (int) getX(startXY.x) : (int) getX(300), differMinute == 0 ? width - 1 : (int) getX(differMinute));
            for (int i = 0; i < size; i++) {
                final XY handicapNow = handicapList.get(i);
                XY handicapNext = null;
                try {
                    handicapNext = handicapList.get(i + 1);
                } catch (Exception ignored) {
                }
                if (handicapNext != null && handicapNext.x > 300) continue;
                if (handicapNext == null) {
                    drawTextCenter(canvas, String.format(Locale.getDefault(), "%.2f", handicapNow.y), startXY != null ? (int) getX(startXY.x) : (int) getX(300), differMinute == 0 ? width - 1 : (int) getX(differMinute));
                } else {
                    drawTextCenter(canvas, String.format(Locale.getDefault(), "%.2f", handicapNow.y), startXY != null ? (int) getX(startXY.x) : (int) getX(300), (int) getX(handicapNext.x));
                    startXY = handicapNext;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawTextCenter(Canvas canvas, String text, int left, int right) {
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Rect rect = new Rect(left, getY(yCenter), right, getHeight() - 1);
        textPaint.setTextSize(25);
        textPaint.setColor(Color.parseColor("#313131"));
        // FontMetrics对象

//        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        // 计算每一个坐标
//        float baseline = rect.top + (rect.bottom - rect.top - (fontMetrics.bottom - fontMetrics.top)) / 2 - fontMetrics.top;
//        float baseline = (rect.bottom + rect.top) / 2;
// 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text == null ? "0.00" : text, rect.centerX(), (rect.top + rect.bottom) / 2, textPaint);
    }

    private void drawHistoryList(Canvas canvas, int width) {
        final float tempStrokeWidth = paint1.getStrokeWidth();
        paint1.setStrokeWidth(HISTORY_STROKE_WIDTH);
        XY lastXY = null, currentXY = null, firstXY = null, startXY = null;
        for (List<XY> list : historyList) {
            boolean isFirstDraw = true;
            for (XY xy : list) {
                paint1.setColor(Color.parseColor("#d2d2d2"));
                if (xy.x < 0 || xy.y < 0) {
                    continue;
                }
                if (startXY == null) {
                    if (xy.x > 300) {
                        startXY = xy;
                    }
                }
                if (lastXY == null) {
                    lastXY = xy;
                    firstXY = lastXY;
                    continue;
                }
                if (xy.x >= 300) {
                    firstXY = xy;
                    continue;
                }
                currentXY = xy;
                if (isFirstDraw) {
                    if (startXY != null) {
                        canvas.drawLine(getX(startXY.x), getY(startXY.y), getX(300), getY(firstXY.y), paint1);
                    }
                    canvas.drawLine(getX(300), getY(firstXY.y), getX(currentXY.x),
                            getY(currentXY.y), paint1);
                    isFirstDraw = false;
                } else {
                    canvas.drawLine(getX(lastXY.x), getY(lastXY.y), getX(currentXY.x),
                            getY(currentXY.y), paint1);
                }
                lastXY = currentXY;
            }
            currentXY = currentXY == null ? lastXY : currentXY;
            if (currentXY == null) {
                return;
            }
            /**
             * 绘制水平线
             */
            if (isFirstDraw) {
                if (startXY != null) {
                    canvas.drawLine(getX(startXY.x), getY(startXY.y), getX(300), getY(firstXY.y), paint1);
                }
                canvas.drawLine(getX(300), getY(firstXY.y), width - 1,
                        getY(firstXY.y), paint1);
            } else {
                canvas.drawLine(getX(currentXY.x), getY(currentXY.y), width - 1,
                        getY(currentXY.y), paint1);
            }
        }
        paint1.setStrokeWidth(tempStrokeWidth);
    }

    private void drawMainList(Canvas canvas, int width) {
        final float tempStrokeWidth = paint1.getStrokeWidth();
        paint1.setStrokeWidth(MAIN_STROKE_WIDTH);
        paint1.setColor(Color.parseColor("#b6a25d"));
        XY lastXY = null, currentXY = null, firstXY = null, startXY = null;
        boolean isFirstDraw = true;
        for (XY xy : mainList) {
            if (xy.x < 0 || xy.y < 0) {
                continue;
            }
            if (startXY == null) {
                if (xy.x > 300) {
                    startXY = xy;
                }
            }
            if (lastXY == null) {
                lastXY = xy;
                firstXY = lastXY;
                continue;
            }
            if (xy.x >= 300) {
                firstXY = xy;
                continue;
            }
            currentXY = xy;
            if (isFirstDraw) {
                if (startXY != null) {
                    canvas.drawLine(getX(startXY.x), getY(startXY.y), getX(300) + 1, getY(firstXY.y), paint1);
                }
                canvas.drawLine(getX(300), getY(firstXY.y), getX(currentXY.x) + 1, getY(currentXY.y), paint1);
                isFirstDraw = false;
            } else {
                canvas.drawLine(getX(lastXY.x), getY(lastXY.y), getX(currentXY.x) + 1, getY(currentXY.y), paint1);
            }
            lastXY = currentXY;
        }
        yCenter = startXY == null ? (firstY + endY) / 3F * 2 : startXY.y;
        if (endY - yCenter <= yCenter - firstY) {
            yBottom = (yCenter + firstY) / 2F;
        } else {
            yBottom = (endY + yCenter) / 2F;
        }
        yTop = endY;

        KLog.e("yTop:" + getTopDataX());
        KLog.e("yCenter:" + getCenterDataX());
        KLog.e("yBottom:" + getBottomDataX());

        if(onDrawListener!=null){
            onDrawListener.onDrawFinished();
        }

        currentXY = currentXY == null ? lastXY : currentXY;
        if (currentXY == null) {
            return;
        }
        /**
         * 绘制水平线
         */
        if (isFirstDraw) {
            if (startXY != null) {
                canvas.drawLine(getX(startXY.x), getY(startXY.y), differMinute == 0 || differMinute < 300 ? getX(300) + 1 : getX(differMinute), getY(firstXY.y), paint1);
            }
            if (differMinute == 0 || differMinute < 300) {
                canvas.drawLine(getX(300), getY(firstXY.y), differMinute == 0 ? width - 1 : getX(differMinute), getY(firstXY.y), paint1);
            }
        } else {
            canvas.drawLine(getX(currentXY.x), getY(currentXY.y), differMinute == 0 ? width - 1 : getX(differMinute), getY(currentXY.y), paint1);
        }
        paint1.setStrokeWidth(tempStrokeWidth);
    }

    private void drawHandicapList(Canvas canvas, int width) {
        XY lastXY = null, currentXY = null, firstXY = null, startXY = null;
        boolean isFirstDraw = true;
        for (XY xy : mainList) {
            if (xy.x < 0 || xy.y < 0) {
                continue;
            }
            if (startXY == null) {
                if (xy.x > 300) {
                    startXY = xy;
                }
            }
            if (lastXY == null) {
                lastXY = xy;
                firstXY = lastXY;
                continue;
            }
            if (xy.x >= 300) {
                firstXY = xy;
                continue;
            }
            currentXY = xy;
            if (isFirstDraw) {
                if (startXY != null) {
                    drawHandicap(canvas, getX(startXY.x), getY(startXY.y), getX(300), getY(firstXY.y));
                }
                drawHandicap(canvas, getX(300), getY(firstXY.y), getX(currentXY.x), getY(currentXY.y));
                isFirstDraw = false;
            } else {
                drawHandicap(canvas, getX(lastXY.x), getY(lastXY.y), getX(currentXY.x), getY(currentXY.y));
            }
            lastXY = currentXY;
        }

        currentXY = currentXY == null ? lastXY : currentXY;
        if (currentXY == null) {
            return;
        }
        /**
         * 绘制水平线
         */
        if (isFirstDraw) {
            if (startXY != null) {
                drawHandicap(canvas, getX(startXY.x), getY(startXY.y), differMinute == 0 || differMinute <= 300 ? getX(300) : getX(differMinute), getY(firstXY.y));
            }
            if (differMinute == 0 || differMinute < 300) {
                drawHandicap(canvas, getX(300), getY(firstXY.y), differMinute == 0 ? width - 1 : getX(differMinute), getY(firstXY.y));
            }
        } else {
            drawHandicap(canvas, getX(currentXY.x), getY(currentXY.y), differMinute == 0 ? width - 1 : getX(differMinute), getY(currentXY.y));
        }
    }

    /**
     * 盘口填充
     */
    private void drawHandicap(Canvas canvas, float startX, float startY, float endX, float endY) {
        final int height = getHeight();
        XY now = null;
        try {
            now = handicapList.get(handicapPosition);
        } catch (Exception ignored) {
        }
        final int lastColor = paint1.getColor();
        paint1.setColor(getHandicapColor());
        if (now == null || now.x >= 300 || getX(now.x) >= endX) {
            final Path path = new Path();
            path.reset();
            path.moveTo(startX, startY);
            path.lineTo(endX, endY);
            path.lineTo(endX, height - 1);
            path.lineTo(startX, height - 1);
            canvas.drawPath(path, paint1);
        } else {
            final float newX = getX(now.x);
            final float scale = (getX(now.x) - startX) * 1F / (endX - startX);
            final float newY = startY + (endY - startY) * scale;
            final Path path = new Path();
            path.reset();
            path.moveTo(startX, startY);
            path.lineTo(newX, newY);
            path.lineTo(newX, height - 1);
            path.lineTo(startX, height - 1);
            canvas.drawPath(path, paint1);
            handicapColorCheck = !handicapColorCheck;
            paint1.setColor(getHandicapColor());
            path.reset();
            path.moveTo(newX, newY);
            path.lineTo(endX, endY);
            path.lineTo(endX, height - 1);
            path.lineTo(newX, height - 1);
            canvas.drawPath(path, paint1);
            handicapPosition++;
        }
        paint1.setColor(lastColor);
    }

    private int getHandicapColor() {
        return handicapColorCheck ? Color.parseColor("#d2c59b") : Color.parseColor("#c9c9c9");
    }

    private void refreshY() {
        float firstY = 100, endY = 0;
        for (XY xy : mainList) {
            if (xy.y < 0) {
                continue;
            }
            firstY = firstY > xy.y ? xy.y : firstY;
            endY = endY < xy.y ? xy.y : endY;
        }
        for (List<XY> list : historyList) {
            for (XY xy : list) {
                if (xy.y < 0) {
                    continue;
                }
                firstY = firstY > xy.y ? xy.y : firstY;
                endY = endY < xy.y ? xy.y : endY;
            }
        }
        firstY -= 0.05;
        endY += 0.05;
        this.firstY = firstY;
        this.endY = endY;
    }

    /**
     * 水平虚线
     */
    private void drawDotLineHorizontal(Canvas canvas, int width, int distance, int x, int y) {
        if (distance < 0) {
            distance = -distance;
        } else if (distance == 0) {
            distance = 1;
        }
        int tempX = x;
        while (tempX < width) {
            canvas.drawLine(tempX, y, tempX + distance, y, paint1);
            tempX += (distance * 2);
        }
    }

    /**
     * 垂直虚线
     */
    private void drawDotLineVertical(Canvas canvas, int height, int distance, int x, int y) {
        if (distance < 0) {
            distance = -distance;
        } else if (distance == 0) {
            distance = 1;
        }
        int tempY = y;
        while (tempY < height) {
            canvas.drawLine(x, tempY, x, tempY + distance, paint1);
            tempY += distance + 3;
        }
    }

    /**
     * 设置显示历史数据
     */
    public void setToShowHistoryList(boolean isToShowHistoryList) {
        this.isToShowHistoryList = isToShowHistoryList;
        invalidate();
    }

    public void setHandicapList(List<XY> handicapList) {
        this.handicapList.clear();
        if (handicapList != null) {
            this.handicapList.addAll(handicapList);
        }
    }

    public void setMainList(List<XY> mainList) {
        this.mainList.clear();
        if (mainList != null) {
            this.mainList.addAll(mainList);
        }
    }

    public void setHistoryList(List<List<XY>> historyList) {
        this.historyList.clear();
        if (historyList != null) {
            this.historyList.addAll(historyList);
        }
//        while (true)
//        {
//            try
//            {
//                this.historyList.remove(10);
//            } catch (Exception e)
//            {
//                break;
//            }
//        }
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    private int getY(float y) {
        final int height = getHeight() - PADDING_TOP;
        return (int) (height * (1 - ((y - firstY) / (endY - firstY)))) + PADDING_TOP;
    }

    private float getX(int x) {
        int result;
        final int width = getWidth();
        final float everyWidth = width / 13;
        final float perMinuteWidth;
        /**
         * 一小时前
         */
        if (x <= 60) {
            perMinuteWidth = everyWidth / 10;
            result = (int) (width - perMinuteWidth * x);
        }
        /**
         * 3小时前
         */
        else if (x <= 180) {
            perMinuteWidth = everyWidth / 30;
            result = (int) (everyWidth * 7 - perMinuteWidth * (x - 60));
        }
        /**
         * 5小时前
         */
        else if (x <= 300) {
            perMinuteWidth = everyWidth / 60;
            result = (int) (everyWidth * 3 - perMinuteWidth * (x - 180));
        }
        /**
         * 29小时前
         */
        else {
            perMinuteWidth = everyWidth / 1440;
            result = (int) (everyWidth - perMinuteWidth * (x - 300));
        }
        return result;
    }

    public String getTopDataX() {
        return String.format("%.2f", yTop);
    }

    public int getTopY() {
        return getY(yTop);
    }

    public String getCenterDataX() {
        return String.format("%.2f", yCenter);
    }

    public int getCenterY() {
        return getY(yCenter);
    }

    public int getBottomY() {
        return getY(yBottom);
    }

    public String getBottomDataX() {
        return String.format("%.2f", yBottom);
    }

    public interface OnDrawListener{
        void onDrawFinished();
    }

    public static class XY {
        final int x;
        final float y;

        public XY(int x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}