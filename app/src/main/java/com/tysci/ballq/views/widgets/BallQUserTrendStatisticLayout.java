package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by HTT on 2016/6/17.
 */
public class BallQUserTrendStatisticLayout extends LinearLayout{
    private TextView tvTrendTitle;
    private TextView tvAllNums;
    private TextView tvAllWinNum;
    private TextView tvAllLoseNum;
    private TextView tvAllGoNum;

    public BallQUserTrendStatisticLayout(Context context) {
        super(context);
        initViews(context);
    }

    public BallQUserTrendStatisticLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public BallQUserTrendStatisticLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BallQUserTrendStatisticLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_user_trend_statistic_item,this,true);
        tvTrendTitle=(TextView)this.findViewById(R.id.tv_trend_title);
        tvAllNums=(TextView)this.findViewById(R.id.tvRoundNum1);
        tvAllWinNum=(TextView)this.findViewById(R.id.tvWinsNum1);
        tvAllLoseNum=(TextView)this.findViewById(R.id.tvLoseNum1);
        tvAllGoNum=(TextView)this.findViewById(R.id.tvGoneNum1);
    }

    public void setAllNum(String value){
        tvAllNums.setText(value);
    }

    public void setAllWinNum(String value){
        tvAllWinNum.setText(value);
    }

    public void setAllLoseNum(String value){
        tvAllLoseNum.setText(value);
    }

    public void setAllGoneNum(String value){
        tvAllGoNum.setText(value);
    }

    public void setTrendTitle(String title){
        tvTrendTitle.setText(title);
    }

    public String getAllNum(){
        return tvAllNums.getText().toString();
    }

    public String getAllWinNum(){
        return tvAllWinNum.getText().toString();
    }

    public String getAllLoseNum(){
        return tvAllLoseNum.getText().toString();
    }

    public String getAllGoneNum(){
        return tvAllGoNum.getText().toString();
    }

    public String getTrendTitle(){
        return tvTrendTitle.getText().toString();
    }
}
