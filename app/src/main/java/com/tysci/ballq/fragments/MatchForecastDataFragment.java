package com.tysci.ballq.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQMatchForecastDataEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQMatchForecastDataAdapter;
import com.tysci.ballq.views.widgets.chartview.LineChartView;
import com.tysci.ballq.views.widgets.chartview.PieChartData;
import com.tysci.ballq.views.widgets.chartview.PieChartView;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/10.
 */
public class MatchForecastDataFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;

    private View headerView=null;

    private BallQMatchEntity matchEntity;
    private int oddsType;
    private int currentPages=1;

    private List<BallQMatchForecastDataEntity.MatchForecastDataEntity> matchForecastDataEntityList=null;
    private BallQMatchForecastDataAdapter adapter=null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.layout_swiperefresh_recyclerview;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        recyclerView.setOnLoadMoreListener(this);
        headerView= LayoutInflater.from(baseActivity).inflate(R.layout.layout_match_forecast_data_header,null);
        recyclerView.addHeaderView(headerView);
        showLoading();
        requestDatas(currentPages, false);
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    public void setMatchEntity(BallQMatchEntity matchEntity) {
        this.matchEntity = matchEntity;
    }

    public void setOddsType(int type){
        this.oddsType=type;
    }

    private void setRefreshing(){
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void onRefreshCompelete(){
        if(swipeRefresh!=null) {
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefresh != null)
                        swipeRefresh.setRefreshing(false);
                }
            }, 1000);
        }
    }

    private void requestDatas(final int pages, final boolean isLoadMore){
        String url="http://apib.ballq.cn/bigdata/oroc/v1/"+matchEntity.getEid()+"/"+oddsType
                   +"/?limit=10&p="+pages;
        KLog.e("url:" + url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(recyclerView!=null) {
                    if (!isLoadMore) {
                        if(adapter!=null) {
                            recyclerView.setStartLoadMore();
                        }else{
                            showErrorInfo(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showLoading();
                                    requestDatas(pages,false);
                                }
                            });
                        }
                    } else {
                        recyclerView.setLoadMoreDataFailed();
                    }
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null){
                        BallQMatchForecastDataEntity entity=obj.getObject("data",BallQMatchForecastDataEntity.class);
                        if(entity!=null){
                            if(!isLoadMore){
                                setPieChartViewDatas(entity);
                                if(matchForecastDataEntityList==null) {
                                    matchForecastDataEntityList = new ArrayList<BallQMatchForecastDataEntity.MatchForecastDataEntity>(10);
                                    adapter=new BallQMatchForecastDataAdapter(matchForecastDataEntityList);
                                    recyclerView.setAdapter(adapter);
                                }
                            }

                            List<BallQMatchForecastDataEntity.MatchForecastDataEntity> matchs=entity.getMatchs();
                            if(matchs!=null&&!matchs.isEmpty()){
                                if(!isLoadMore&&!matchForecastDataEntityList.isEmpty()){
                                    matchForecastDataEntityList.clear();
                                }
                                matchForecastDataEntityList.addAll(matchs);
                                adapter.notifyDataSetChanged();
                                if(matchs.size()<10){
                                    recyclerView.setLoadMoreDataComplete("没有更多数据了");
                                }else{
                                    recyclerView.setStartLoadMore();
                                    if(isLoadMore){
                                        currentPages++;
                                    }else{
                                        currentPages=2;
                                    }
                                }
                            }else{
                                if(matchForecastDataEntityList.isEmpty()){
                                    recyclerView.setLoadMoreDataComplete();
                                    headerView.findViewById(R.id.layout_forecast_data).setVisibility(View.GONE);
                                }else{
                                    recyclerView.setLoadMoreDataComplete("没有更多数据了");
                                    headerView.findViewById(R.id.layout_forecast_data).setVisibility(View.VISIBLE);
                                }
                            }
                            return;
                        }
                    }
                }
                showEmptyInfo();
            }

            @Override
            public void onFinish(Call call) {
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                    onRefreshCompelete();
                }
            }
        });
    }

    private void setPieChartViewDatas(BallQMatchForecastDataEntity data){
        List<PieChartData> pieChartDataList=new ArrayList<>(3);
        if(data.getRate1()+data.getRate2()+data.getRate3()==0){
            PieChartData pie01=new PieChartData();
            pie01.setValue(1/3);
            pie01.setLabel(String.format(Locale.getDefault(), "%.0f", 100 * pie01.getValue()) + "%");
            pie01.setColor(Color.parseColor("#ca574b"));
            pieChartDataList.add(pie01);

            PieChartData pie02=new PieChartData();
            pie02.setValue(1/3);
            pie02.setLabel(String.format(Locale.getDefault(), "%.0f", 100 * pie02.getValue()) + "%");
            pie02.setColor(Color.parseColor("#c3c3c3"));
            pieChartDataList.add(pie02);

            PieChartData pie03=new PieChartData();
            pie03.setValue(1/3);
            pie03.setLabel(String.format(Locale.getDefault(), "%.0f", 100 * pie03.getValue()) + "%");
            pie03.setColor(Color.parseColor("#66b249"));
            pieChartDataList.add(pie03);

        }else{
            PieChartData pie01=new PieChartData();
            pie01.setValue(data.getRate1());
            pie01.setLabel(String.format(Locale.getDefault(), "%.0f", 100 * pie01.getValue()) + "%");
            pie01.setColor(Color.parseColor("#ca574b"));
            pieChartDataList.add(pie01);

            PieChartData pie02=new PieChartData();
            pie02.setValue(data.getRate2());
            pie02.setLabel(String.format(Locale.getDefault(), "%.0f", 100 * pie02.getValue()) + "%");
            pie02.setColor(Color.parseColor("#c3c3c3"));
            pieChartDataList.add(pie02);

            PieChartData pie03=new PieChartData();
            pie03.setValue(data.getRate3());
            pie03.setLabel(String.format(Locale.getDefault(), "%.0f", 100 * pie03.getValue()) + "%");
            pie03.setColor(Color.parseColor("#66b249"));
            pieChartDataList.add(pie03);
        }

        PieChartView pieChartView= (PieChartView) headerView.findViewById(R.id.piechartview);
        pieChartView.setPieChartDataList(pieChartDataList);
    }

    private void setMatchLineChartData(BallQMatchForecastDataEntity data){
        if(data!=null){
            final LineChartView lineChartView= (LineChartView) (headerView).findViewById(R.id.lineChartView);
            lineChartView.setToShowHistoryList(true);
            final TextView lineTopDataX = (TextView) headerView.findViewById(R.id.topDataX);
            final TextView lineCenterDataX = (TextView) headerView.findViewById(R.id.centerDataX);
            final TextView lineBottomDataX = (TextView) headerView.findViewById(R.id.bottomDataX);
            List<BallQMatchForecastDataEntity.MatchForecastDataEntity> matchs=data.getMatchs();
            final List<List<LineChartView.XY>> historyList = new ArrayList<>();
            boolean isFirst=true;
            for(BallQMatchForecastDataEntity.MatchForecastDataEntity m:matchs){
                final List<String> odds = m.getRt_odds();
                if (isFirst) {
                    /**
                     * 主数据
                     */
                    final List<LineChartView.XY> mainList = new ArrayList<>();
                    try {
                        for (String odd : odds) {
                            try {
                                final String[] temp = odd.split("@");
                                final LineChartView.XY xy = new LineChartView.XY(Integer.parseInt(temp[0]), Float.parseFloat(temp[1]));
                                mainList.add(xy);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    lineChartView.setMainList(mainList);
                    lineChartView.setResultDate(m.getMatch_date());
                    /**
                     * 主盘口
                     */
                    final List<LineChartView.XY> fillList = new ArrayList<>();
                    try {
                        for (String pan : m.getHandicap_records()) {
                            try {
                                final String[] temp = pan.split("@");
                                final LineChartView.XY xy = new LineChartView.XY(Integer.parseInt(temp[0]), Float.parseFloat(temp[1]));
                                fillList.add(xy);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    lineChartView.setHandicapList(fillList);
                    isFirst = false;
                } else {
                    /**
                     * 历史数据
                     */
                    final List<LineChartView.XY> history = new ArrayList<>();
                    try {
                        for (String odd : odds) {
                            try {
                                final String[] temp = odd.split("@");
                                final LineChartView.XY xy = new LineChartView.XY(Integer.parseInt(temp[0]), Float.parseFloat(temp[1]));
                                history.add(xy);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    historyList.add(history);
                }
            }
            lineChartView.setHistoryList(historyList);
            /**
             * 刷新折线图
             */
            lineChartView.postInvalidate();

            lineChartView.setOnDrawListener(new LineChartView.OnDrawListener() {
                @Override
                public void onDrawFinished() {
                    RelativeLayout.LayoutParams lp;
                    // TOP
                    lp = (RelativeLayout.LayoutParams) lineTopDataX.getLayoutParams();
                    lp.topMargin = lineChartView.getTopY() - 10;
                    lineTopDataX.setLayoutParams(lp);
                    KLog.e("TopDataX:" + lineChartView.getTopDataX());
                    lineTopDataX.setText(lineChartView.getTopDataX());
                    // CENTER
                    lp = (RelativeLayout.LayoutParams) lineCenterDataX.getLayoutParams();
                    lp.topMargin = lineChartView.getCenterY() - 10;
                    lineCenterDataX.setLayoutParams(lp);
                    KLog.e("CenterDataX:" + lineChartView.getCenterDataX());
                    lineCenterDataX.setText(lineChartView.getCenterDataX());
                    // BOTTOM
                    lp = (RelativeLayout.LayoutParams) lineBottomDataX.getLayoutParams();
                    lp.topMargin = lineChartView.getBottomY() - 10;
                    lineBottomDataX.setLayoutParams(lp);
                    KLog.e("BottomDataX:" + lineChartView.getBottomDataX());
                    lineBottomDataX.setText(lineChartView.getBottomDataX());
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        if(recyclerView.isLoadMoreing()){
            onRefreshCompelete();
        }else{
            recyclerView.setRefreshing();
            requestDatas(1, false);
        }
    }

    @Override
    public void onLoadMore() {
        if(recyclerView.isRefreshing()){
            recyclerView.setRefreshingTip("刷新数据中...");
        }else{
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestDatas(currentPages,true);
                }
            },300);
        }
    }

    @Override
    protected boolean isCancledEventBus() {
        return false;
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }
}
