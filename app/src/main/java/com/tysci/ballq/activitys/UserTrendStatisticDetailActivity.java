package com.tysci.ballq.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQTrendProfitStatisticEntity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.BallQTrendProfitStatisticLayout;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/6/17.
 */
public class UserTrendStatisticDetailActivity extends BaseActivity
{
    @Bind(R.id.ivUserIcon)
    protected CircleImageView ivUserIcon;
    @Bind(R.id.isV)
    protected ImageView isV;
    @Bind(R.id.tv_user_name)
    protected TextView tvUserName;
    @Bind(R.id.tv_user_profit_rate)
    protected TextView tvUserProfitRate;
    @Bind(R.id.tvTrendProfit)
    protected TextView tvTrendProfit;
    @Bind(R.id.tvTrendProfitably)
    protected TextView tvTrendProfitabley;
    @Bind(R.id.tv_all_count)
    protected TextView tvAllCount;
    @Bind(R.id.tv_win_count)
    protected TextView tvWinCount;
    @Bind(R.id.tv_lose_count)
    protected TextView tvLoseCount;
    @Bind(R.id.tv_go_count)
    protected TextView tvGoCount;
    @Bind(R.id.textView1)
    protected TextView textView1;
    @Bind(R.id.textView2)
    protected TextView textView2;
    @Bind(R.id.textView3)
    protected TextView textView3;
    @Bind(R.id.textView4)
    protected TextView textView4;
    @Bind(R.id.textView5)
    protected TextView textView5;
    @Bind(R.id.textView6)
    protected TextView textView6;
    @Bind(R.id.layout_trend_ahc_profit)
    protected BallQTrendProfitStatisticLayout trendAhc;
    @Bind(R.id.layout_trend_league_profit)
    protected BallQTrendProfitStatisticLayout trendLeague;
    @Bind(R.id.layout_trend_month_profit)
    protected BallQTrendProfitStatisticLayout trendMonth;
    @Bind(R.id.layout_trend_to_profit)
    protected BallQTrendProfitStatisticLayout trendTo;
    @Bind(R.id.layout_trend_gold_profit)
    protected BallQTrendProfitStatisticLayout trendGold;
    @Bind(R.id.layout_trend_week_profit)
    protected BallQTrendProfitStatisticLayout trendWeek;
    @Bind(R.id.lineChartView)
    protected LineChart lineChartView;

    private String uid = null;
    private int etype = -1;
    private boolean isOldUser = false;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_user_trend_statistics_detail;
    }

    @Override
    protected void initViews()
    {
        trendAhc.setTrendProfitTitle("按亚盘统计盈利");
        trendLeague.setTrendProfitTitle("按赛事统计盈利");
        trendMonth.setTrendProfitTitle("按月份统计盈利");
        trendTo.setTrendProfitTitle("按大小球统计盈利");
        trendGold.setTrendProfitTitle("按球币统计盈利");
        trendWeek.setTrendProfitTitle("按天统计盈利");
        tvUserProfitRate.setVisibility(View.GONE);

        lineChartView.setDescription("");
        lineChartView.setDragEnabled(false);
        lineChartView.setTouchEnabled(false);
        lineChartView.setDrawBorders(false);
        lineChartView.setDrawGridBackground(false);
        lineChartView.setNoDataText("暂无相关数据");

        lineChartView.getLegend().setEnabled(false);
        lineChartView.getAxis(YAxis.AxisDependency.RIGHT).setEnabled(false);

    }

    @Override
    protected View getLoadingTargetView()
    {
        return this.findViewById(R.id.layout_trend_static_info);
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        isOldUser = intent.getBooleanExtra("old_user", false);
        uid = intent.getStringExtra("user_id");
        etype = intent.getIntExtra("etype", -1);
        setTitle(intent.getStringExtra("trend_title"));
        tvAllCount.setText(intent.getStringExtra("trend_all") + " 场");
        String winCount = intent.getStringExtra("trend_win");
        CommonUtils.setTextViewFormatString(tvWinCount, winCount + " 场", winCount, Color.parseColor("#e35354"), 1f);

        String loseCount = intent.getStringExtra("trend_lose");
        CommonUtils.setTextViewFormatString(tvLoseCount, loseCount + " 场", loseCount, Color.parseColor("#469c4a"), 1f);

        String goneCount = intent.getStringExtra("trend_gone");
        CommonUtils.setTextViewFormatString(tvGoCount, goneCount + " 场", goneCount, Color.parseColor("#d4d4d4"), 1f);

        UserInfoEntity userInfo = intent.getParcelableExtra("user_info");
        if (userInfo != null)
        {
            GlideImageLoader.loadImage(this, userInfo.getPt(), R.mipmap.icon_user_default, ivUserIcon);
            UserInfoUtil.setUserHeaderVMark(userInfo.getIsv(), isV, ivUserIcon);
            tvUserName.setText(userInfo.getFname());
        }

        if (!TextUtils.isEmpty(uid))
        {
            showLoading();
            getTrendStatisticDetailInfo(etype, uid);
        }

    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return false;
    }

    private void getTrendStatisticDetailInfo(final int etype, final String uid)
    {
        String url = HttpUrls.HOST_URL_V5 + (isOldUser ? "old/" : "") + "user/" + uid + "/betting_stats/" + (etype >= 0 ? "?etype=" + etype : "");
        KLog.e("url:" + url);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, null, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {
                showErrorInfo(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showLoading();
                        getTrendStatisticDetailInfo(etype, uid);
                    }
                });

            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                hideLoad();
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null)
                    {
                        JSONObject dataObj = obj.getJSONObject("data");
                        if (dataObj != null && !dataObj.isEmpty())
                        {
                            JSONObject generalObj = dataObj.getJSONObject("general");
                            if (generalObj != null && !generalObj.isEmpty())
                            {
                                setTrendStataisticInfo(generalObj);
                            }

                            JSONArray ahcArrays = dataObj.getJSONArray("ahc");
                            if (ahcArrays != null && !ahcArrays.isEmpty())
                            {
                                List<BallQTrendProfitStatisticEntity> ahcs = new ArrayList<>(ahcArrays.size());
                                CommonUtils.getJSONListObject(ahcArrays, ahcs, BallQTrendProfitStatisticEntity.class);
                                setTrendProfitType(1, ahcs);
                                trendAhc.setTrendProfitStatistValue("ahc", "ahc_type", etype, isOldUser, ahcs);
                            }

                            JSONArray tournArrays = dataObj.getJSONArray("tourn");
                            if (tournArrays != null && !tournArrays.isEmpty())
                            {
                                List<BallQTrendProfitStatisticEntity> tourns = new ArrayList<>();
                                CommonUtils.getJSONListObject(tournArrays, tourns, BallQTrendProfitStatisticEntity.class);
                                setTrendProfitType(2, tourns);
                                trendLeague.setTrendProfitStatistValue("tourn", "tournid", etype, isOldUser, tourns);
                            }

                            JSONArray monthArrays = dataObj.getJSONArray("month");
                            if (monthArrays != null && !monthArrays.isEmpty())
                            {
                                List<BallQTrendProfitStatisticEntity> months = new ArrayList<>();
                                CommonUtils.getJSONListObject(monthArrays, months, BallQTrendProfitStatisticEntity.class);
                                setTrendProfitType(3, months);
                                trendMonth.setTrendProfitStatistValue("month", "month", etype, isOldUser, months);
                                setLineChartViewData(months);
                            }

                            JSONArray toArrays = dataObj.getJSONArray("to");
                            if (toArrays != null && !toArrays.isEmpty())
                            {
                                List<BallQTrendProfitStatisticEntity> tos = new ArrayList<>();
                                CommonUtils.getJSONListObject(toArrays, tos, BallQTrendProfitStatisticEntity.class);
                                setTrendProfitType(4, tos);
                                trendTo.setTrendProfitStatistValue("to", "to_type", etype, isOldUser, tos);
                            }

                            JSONArray amountArrays = dataObj.getJSONArray("amount");
                            if (amountArrays != null && !amountArrays.isEmpty())
                            {
                                List<BallQTrendProfitStatisticEntity> amounts = new ArrayList<>();
                                CommonUtils.getJSONListObject(amountArrays, amounts, BallQTrendProfitStatisticEntity.class);
                                setTrendProfitType(5, amounts);
                                trendGold.setTrendProfitStatistValue("amount", "sam", etype, isOldUser, amounts);
                            }

                            JSONArray weekArrays = dataObj.getJSONArray("weekday");
                            if (weekArrays != null && !weekArrays.isEmpty())
                            {
                                List<BallQTrendProfitStatisticEntity> weeks = new ArrayList<>();
                                CommonUtils.getJSONListObject(weekArrays, weeks, BallQTrendProfitStatisticEntity.class);
                                setTrendProfitType(6, weeks);
                                trendWeek.setTrendProfitStatistValue("weekday", "weekday", etype, isOldUser, weeks);
                            }
                        }
                    }
                }
                //showEmptyInfo();
            }

            @Override
            public void onFinish(Call call)
            {

            }
        });
    }

    private void setTrendStataisticInfo(JSONObject general)
    {
        String tempStr;

        tempStr = general.getString("fname");
        if (!TextUtils.isEmpty(tempStr))
            tvUserName.setText(tempStr);

        tempStr = general.getString("pt");
        if (!TextUtils.isEmpty(tempStr))
        {
            GlideImageLoader.loadImage(this, tempStr, R.mipmap.icon_user_default, ivUserIcon);
            UserInfoUtil.setUserHeaderVMark(general.getIntValue("isv"), isV, ivUserIcon);
        }

        tempStr = String.format(Locale.getDefault(), "%.2f", general.getFloat("tearn") / 100F);
        if (tempStr.equalsIgnoreCase("NaN")) tempStr = "0.00";
        tvTrendProfit.setText(tempStr);

        tempStr = String.format(Locale.getDefault(), "%.2f", general.getFloat("ror")) + "%";
        if (tempStr.equalsIgnoreCase("NaN%")) tempStr = "0.00%";
        tvTrendProfitabley.setText(tempStr);

        tempStr = String.format(Locale.getDefault(), "%.0f", general.getFloat("wins") * 100F) + "%";
        if (tempStr.equalsIgnoreCase("NaN%")) tempStr = "0.00%";
        textView1.setText(tempStr);

        tempStr = String.format(Locale.getDefault(), "%.2f", general.getFloat("avgamt") / 100F);
        if (tempStr.equalsIgnoreCase("NaN")) tempStr = "0.00";
        textView2.setText(tempStr);

        tempStr = String.format(Locale.getDefault(), "%.2f", general.getFloat("avgrate"));
        if (tempStr.equalsIgnoreCase("NaN")) tempStr = "0.00";
        textView3.setText(tempStr);

        tempStr = String.format(Locale.getDefault(), "%.2f", general.getFloat("betamt") / 100F);
        if (tempStr.equalsIgnoreCase("NaN")) tempStr = "0.00";
        textView4.setText(tempStr);

        tempStr = String.format(Locale.getDefault(), "%.2f", general.getFloat("retamt") / 100F);
        if (tempStr.equalsIgnoreCase("NaN")) tempStr = "0.00";
        textView5.setText(tempStr);
        setTrendInfo(textView6, general.getString("trends"));
    }

    /**
     * 这个方法用来添加走势里面的数据，赢用红色＋号表示，输用绿色－号表示，走用灰色0表示
     * params tv:  显示走势的TextView
     * params s :  服务端传过来的走势数据，赢为1，输为2，走为3
     */
    private void setTrendInfo(TextView tv, String s)
    {
        try
        {
            char c;
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            for (int i = 0; i < s.length(); i++)
            {
                c = s.charAt(i);
                if ((int) c == (int) '1')
                {
                    ssb.append('+');
                    ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ce483d")), i, i + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                if ((int) c == (int) '2')
                {
                    ssb.append('-');
                    ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#469c4a")), i, i + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                if ((int) c == (int) '3')
                {
                    ssb.append('0');
                    ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#9b9b9b")), i, i + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }
            tv.setText(ssb);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setTrendProfitType(int type, List<BallQTrendProfitStatisticEntity> datas)
    {
        if (datas != null && !datas.isEmpty())
        {
            int size = datas.size();
            for (int i = 0; i < size; i++)
            {
                datas.get(i).setType(type);
            }
        }
    }

//    private void setLineChartViewData(List<BallQTrendProfitStatisticEntity>datas){
//        if(datas!=null&&datas.size()>0){
//            lineChartView.setVisibility(View.VISIBLE);
//            LineSet lineSet=new LineSet();
//            String[]dates=datas.get(0).getMonth().split("-");
//            final int year = Integer.parseInt(dates[0]);
//            String endMonth=String.valueOf(year + "/" + dates[dates.length-1]);
//            String startMonth=String.valueOf((year - 1) + "/" + dates[dates.length-1]);
//
//            int size=datas.size();
//            int count=13-size;
//            float min=0f;
//            float max=0f;
//            for(int i=0;i<count;i++){
//                if(i==0) {
//                    lineSet.addPoint(startMonth,0f);
//                }else{
//                    lineSet.addPoint("",0f);
//                }
//            }
//            for(int i=size-1;i>=0;i--){
//                float value=0f;
//                if(i+1<size){
//                    value=(float) (datas.get(i).getEarn() + datas.get(i + 1).getEarn()) / 100f;
//                    if(i==0) {
//                        lineSet.addPoint(endMonth, value);
//                    }else{
//                        lineSet.addPoint("", value);
//                    }
//                }else{
//                    value=(float)datas.get(i).getEarn()/100f;
//                    lineSet.addPoint("",value);
//                }
//                KLog.e("value:"+value);
//                if(min>value){
//                    min=value;
//                }
//                if(max<value){
//                    max=value;
//                }
//            }
//
//            lineSet.setSmooth(true);
//            lineSet
//                    .setDotsColor(Color.parseColor("#ffc755"))
//                    .setThickness(4).beginAt(0);
//            lineChartView.addData(lineSet);
//            KLog.e("max:"+max);
//            KLog.e("min:"+min);
//            int maxValue= (int) Math.ceil(max);
//            int minValue=(int)Math.floor(min);
//            KLog.e("MaxValue:"+maxValue);
//            KLog.e("MinValue:"+minValue);
//            lineChartView.setBorderSpacing(Tools.fromDpToPx(0))
//                    .setAxisBorderValues(minValue-10,maxValue+10,(maxValue-minValue)/4+20)
//                    .setYLabels(AxisController.LabelPosition.OUTSIDE)
//                    .setXLabels(AxisController.LabelPosition.OUTSIDE)
//                    .setLabelsColor(Color.parseColor("#3a3a3a"))
//                    .setXAxis(true)
//                    .setYAxis(false);
//            lineChartView.show();
//        }else{
//            lineChartView.setVisibility(View.GONE);
//        }
//    }

    private void setLineChartViewData(List<BallQTrendProfitStatisticEntity> datas)
    {
        if (datas != null && datas.size() > 0)
        {
            lineChartView.setVisibility(View.VISIBLE);
            String[] dates = datas.get(0).getMonth().split("-");
            final int year = Integer.parseInt(dates[0]);
            String endMonth = String.valueOf(year + "/" + dates[dates.length - 1]);
            String startMonth = String.valueOf((year - 1) + "/" + dates[dates.length - 1]);

            List<String> xVals = new ArrayList<>(13);
            List<Entry> yVals = new ArrayList<>(13);

            int size = datas.size();
            int count = 13 - size;
            float min = 0f;
            float max = 0f;
            for (int i = 0; i < count; i++)
            {
                if (i == 0)
                {
                    //lineSet.addPoint(startMonth,0f);
                    xVals.add(startMonth);
                    yVals.add(new Entry(0f, yVals.size()));
                }
                else
                {
                    //lineSet.addPoint("",0f);
                    xVals.add("");
                    yVals.add(new Entry(0f, yVals.size()));
                }
            }
            for (int i = size - 1; i >= 0; i--)
            {
                float value = 0f;
                if (i + 1 < size)
                {
                    value = (float) (datas.get(i).getEarn() + datas.get(i + 1).getEarn()) / 100f;
                    if (i == 0)
                    {
                        //lineSet.addPoint(endMonth, value);
                        xVals.add(endMonth);
                        yVals.add(new Entry(value, yVals.size()));
                    }
                    else
                    {
                        xVals.add("");
                        yVals.add(new Entry(value, yVals.size()));
                    }
                }
                else
                {
                    if (size == 1)
                    {
                        value = (float) (datas.get(i).getEarn() + datas.get(0).getEarn()) / 100f;
                        xVals.add(endMonth);
                        yVals.add(new Entry(value, yVals.size()));
                    }
                    else
                    {
                        value = (float) datas.get(i).getEarn() / 100f;
                        //lineSet.addPoint("",value);
                        xVals.add("");
                        yVals.add(new Entry(value, yVals.size()));
                    }
                }
                KLog.e("value:" + value);
                if (min > value)
                {
                    min = value;
                }
                if (max < value)
                {
                    max = value;
                }
            }

//            lineSet.setSmooth(true);
//            lineSet
//                    .setDotsColor(Color.parseColor("#ffc755"))
//                    .setThickness(4).beginAt(0);
//            lineChartView.addData(lineSet);
            KLog.e("max:" + max);
            KLog.e("min:" + min);
            int maxValue = (int) Math.ceil(max) + 10;
            int minValue = (int) Math.floor(min) - 10;
            KLog.e("MaxValue:" + maxValue);
            KLog.e("MinValue:" + minValue);

            LineDataSet dataSet = new LineDataSet(yVals, null);

            dataSet.setLineWidth(1.75f); // 线宽
            dataSet.setCircleSize(3f);// 显示的圆形大小
            dataSet.setColor(Color.parseColor("#d3bd6e"));// 显示颜色
            dataSet.setCircleColor(Color.parseColor("#d3bd6e"));// 圆形的颜色
            dataSet.setHighLightColor(Color.parseColor("#eacb70")); // 高亮的线的颜色

            dataSet.setDrawFilled(true);//设置折线下方是否填充颜色
            dataSet.setFillColor(Color.parseColor("#f2efdf"));//设置填充色值
            dataSet.setFillAlpha(220);//设置填充透明度
            dataSet.setFillFormatter(new FillFormatter()
            {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider)
                {
                    return 0f;
                }
            });
            dataSet.setValueTextSize(10f);
            dataSet.setValueFormatter(new ValueFormatter()
            {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
                {
                    return String.format(Locale.getDefault(), "%.0f", value);
                }
            });

            XAxis xAxis = lineChartView.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setSpaceBetweenLabels(1);
            xAxis.setLabelsToSkip(1);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setTextSize(12f);


            YAxis yAxis = lineChartView.getAxis(YAxis.AxisDependency.LEFT);
            ;
            yAxis.setLabelCount(4, true);
            yAxis.setDrawZeroLine(true);
            yAxis.setDrawAxisLine(false);
            yAxis.setAxisMinValue(minValue);
            yAxis.setAxisMaxValue(maxValue);
            yAxis.setSpaceBottom(1f);
            yAxis.setSpaceTop(1f);
            yAxis.setTextSize(12f);

            LineData lineData = new LineData(xVals, dataSet);

            lineChartView.setData(lineData);
            lineChartView.animateXY(3000, 3000);
        }
        else
        {
            lineChartView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.layout_user_header_info)
    protected void lookUserInfo(View view)
    {
        UserInfoUtil.lookUserInfo(this, Integer.parseInt(uid));
    }

    @Override
    protected void saveInstanceState(Bundle outState)
    {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState)
    {

    }

    @Override
    protected void onViewClick(View view)
    {

    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {

    }
}
