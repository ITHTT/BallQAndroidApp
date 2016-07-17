package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQGoBettingRecordEntity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQGoBettingRecordAdapter;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderDecoration;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/15.
 * 历史战绩
 */
public class BallQGoGreatWarHistoryFragment extends BaseFragment implements AutoLoadMoreRecyclerView.OnLoadMoreListener
        ,SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.layout_user_info)
    protected FrameLayout layoutUserInfo;
    @Bind(R.id.tv_win_count)
    protected TextView tvWinCount;
    @Bind(R.id.tv_lose_count)
    protected TextView tvLoseCount;
    @Bind(R.id.tv_go_count)
    protected TextView tvGoCount;
    @Bind(R.id.tv_all_count)
    protected TextView tvAllCount;
    @Bind(R.id.tv_win_moneys)
    protected TextView tvWinMoneys;
    @Bind(R.id.tv_lose_moneys)
    protected TextView tvLoseMoneys;
    @Bind(R.id.tv_go_moneys)
    protected TextView tvGoMoneys;
    @Bind(R.id.tv_all_moneys)
    protected TextView tvAllMoneys;
    @Bind(R.id.tv_betting_info)
    protected TextView tvBettingInfo;
    @Bind(R.id.ivUserIcon)
    protected CircleImageView ivUserIcon;
    @Bind(R.id.isV)
    protected ImageView ivIsV;
    @Bind(R.id.tv_user_name)
    protected TextView tvUserName;
    @Bind(R.id.line01)
    protected View line01;
    @Bind(R.id.line02)
    protected View line02;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;

    private int currentPages=1;
    private List<BallQGoBettingRecordEntity> ballQGoBettingRecordEntityList;
    private BallQGoBettingRecordAdapter adapter=null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_great_war_go_history;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        layoutUserInfo.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        recyclerView.setOnLoadMoreListener(this);
        swipeRefresh.setOnRefreshListener(this);
        if(UserInfoUtil.checkLogin(baseActivity)) {
            showLoading();
            requestUserInfo();
        }else{
            showEmptyInfo("您尚未登录,登录后才可查看", "点击登录", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoUtil.userLogin(baseActivity);
                }
            });
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    private void onRefreshCompelete(){
        swipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(swipeRefresh!=null)
                    swipeRefresh.setRefreshing(false);
            }
        }, 1000);
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

    @Override
    public void onRefresh() {
        if (recyclerView.isLoadMoreing()) {
            onRefreshCompelete();
        } else {
            recyclerView.setRefreshing();
            requestUserInfo();
        }
    }

    @Override
    public void onLoadMore() {
        if (recyclerView.isRefreshing()) {
            KLog.e("刷新数据中....");
            recyclerView.setRefreshingTip("刷新数据中...");
        } else {
            KLog.e("currentPage:" + currentPages);
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestBettingRecordListDatas(currentPages,true);
                }
            }, 300);
        }
    }

    private void requestUserInfo(){
        String url= HttpUrls.HOST_URL_V5+"go/user/stastics/";
        HashMap<String,String> params=new HashMap<>(2);
        params.put("user", UserInfoUtil.getUserId(baseActivity));
        params.put("token", UserInfoUtil.getUserToken(baseActivity));
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                onRefreshCompelete();
                if(adapter==null){
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestUserInfo();
                        }
                    });
                }else{
                    recyclerView.setStartLoadMore();
                    ToastUtil.show(baseActivity,"请求失败");
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                setUserInfo(response);
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void setUserInfo(String response){
        if(!TextUtils.isEmpty(response)){
                JSONObject resObj= JSONObject.parseObject(response);
                if(resObj!=null&&!resObj.isEmpty()){
                    int status=resObj.getIntValue("status");
                    if(status==0){
                        JSONArray objArray=resObj.getJSONArray("data");
                        if(objArray!=null&&!objArray.isEmpty()) {
                            JSONObject obj = objArray.getJSONObject(0);
                            if (obj != null) {
                                layoutUserInfo.setVisibility(View.VISIBLE);
                                line01.setVisibility(View.VISIBLE);
                                line02.setVisibility(View.VISIBLE);
                                GlideImageLoader.loadImage(baseActivity,obj.getString("pt"),R.mipmap.icon_user_default,ivUserIcon);
                                tvUserName.setText(obj.getString("fname"));
                                UserInfoUtil.setUserHeaderVMark(obj.getIntValue("isv"),ivIsV,ivUserIcon);
                                String win_count = obj.getString("win");
                                if (!TextUtils.isEmpty(win_count) && TextUtils.isDigitsOnly(win_count)) {
                                    tvWinCount.setText(win_count + "赢");
                                } else {
                                    tvWinCount.setText("0赢");
                                }
                                String lose_count = obj.getString("lose");
                                if (!TextUtils.isEmpty(lose_count) && TextUtils.isDigitsOnly(lose_count)) {
                                    tvLoseCount.setText(lose_count + "输");
                                } else {
                                    tvLoseCount.setText("0输");
                                }
                                String go_count = obj.getString("go");
                                if (!TextUtils.isEmpty(go_count) && TextUtils.isDigitsOnly(go_count)) {
                                    tvGoCount.setText(go_count + "走");
                                } else {
                                    tvGoCount.setText("0走");
                                }
                                String total = obj.getString("total");
                                if (!TextUtils.isEmpty(total) && TextUtils.isDigitsOnly(total)) {
                                    tvAllCount.setText("共" + total + "场");
                                } else {
                                    tvAllCount.setText("共0场");
                                }

                                String win = obj.getString("win_amount");
                                if (!TextUtils.isEmpty(win) && !win.equals("null")) {
                                    tvWinMoneys.setText(String.format("%.2f", Float.parseFloat(win) / 100) + "金币");
                                } else {
                                    tvWinMoneys.setText("0.00金币");
                                }
                                String lose = obj.getString("lose_amount");
                                if (!TextUtils.isEmpty(lose) && !lose.equals("null")) {
                                    tvLoseMoneys.setText(String.format("%.2f", Float.parseFloat(lose) / 100) + "金币");
                                } else {
                                    tvLoseMoneys.setText("0.00金币");
                                }
                                String go = obj.getString("go_go_amount");
                                if (!TextUtils.isEmpty(go) && !go.equals("null")) {
                                    tvGoMoneys.setText(String.format("%.2f", Float.parseFloat(go) / 100) + "金币");
                                } else {
                                    tvGoMoneys.setText("0.00金币");
                                }
                                String total_amount = obj.getString("profit");
                                if (!TextUtils.isEmpty(total_amount) && !total_amount.equals("null")) {
                                    tvAllMoneys.setText("共" + String.format("%.2f", Float.parseFloat(total_amount) / 100) + "金币");
                                } else {
                                    tvAllMoneys.setText("共0.00金币");
                                }

                                String yield_gap = obj.getString("yield_gap");
                                if (!TextUtils.isEmpty(yield_gap) && !yield_gap.equals("null")) {
                                    float gaps = Float.parseFloat(yield_gap) / 100;
                                    KLog.e("gaps:" + gaps);
                                    String info = "与球商GO比较:";
                                    if (gaps >= 0) {
                                        info = info + "+" + String.format("%.2f", gaps) + "金币";
                                    } else {
                                        info = info + String.format("%.2f", gaps) + "金币";
                                    }
                                    tvBettingInfo.setText(info);
                                } else {
                                    tvBettingInfo.setText("与球商GO比较:0.00金币");
                                }
                                requestBettingRecordListDatas(1, false);
                                return;
                            }
                        }
                    }

                }
        }
        showEmptyInfo();
    }

    private void requestBettingRecordListDatas(final int pages, final boolean isLoadMore){
        String url=HttpUrls.HOST_URL_V5+"go/user/bets/?p="+pages;
        HashMap<String,String>params=new HashMap<>(2);
        params.put("user", UserInfoUtil.getUserId(baseActivity));
        params.put("token", UserInfoUtil.getUserToken(baseActivity));
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(isLoadMore){
                    recyclerView.setLoadMoreDataFailed();
                }else {
                    if (adapter == null) {
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestBettingRecordListDatas(pages,isLoadMore);
                            }
                        });
                    } else {
                        recyclerView.setStartLoadMore();
                        ToastUtil.show(baseActivity,"请求失败");
                    }
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                }
                onResponseSuccess(response,isLoadMore);
            }

            @Override
            public void onFinish(Call call) {
                if(!isLoadMore){
                    onRefreshCompelete();
                }
            }
        });
    }

    protected void onResponseSuccess(String response, boolean isLoadMore){
        if(!TextUtils.isEmpty(response)){
            JSONObject obj=JSONObject.parseObject(response);
            if(obj!=null&&!obj.isEmpty()){
                int status=obj.getIntValue("status");
                if(status==0){
                    JSONArray datas=obj.getJSONArray("data");
                    if(datas!=null&&!datas.isEmpty()){
                        if(!isLoadMore){
                            hideLoad();
                        }
                        if(ballQGoBettingRecordEntityList==null){
                            ballQGoBettingRecordEntityList=new ArrayList<>(10);
                        }
                        if(!isLoadMore&&!ballQGoBettingRecordEntityList.isEmpty()){
                            ballQGoBettingRecordEntityList.clear();
                        }
                       getJSONDatas(datas,ballQGoBettingRecordEntityList);
                        KLog.e("Size:"+ballQGoBettingRecordEntityList.size());
                        if(adapter==null){
                            adapter=new BallQGoBettingRecordAdapter(ballQGoBettingRecordEntityList);
                            StickyHeaderDecoration stickyHeaderDecoration=new StickyHeaderDecoration(adapter);
                            recyclerView.setAdapter(adapter);
                            recyclerView.addItemDecoration(stickyHeaderDecoration);
                        }else{
                            adapter.notifyDataSetChanged();
                        }

                        if(datas.size()<10){
                            recyclerView.setLoadMoreDataComplete("没有更多数据了");
                        }else{
                            recyclerView.setStartLoadMore();
                            if(isLoadMore) {
                                currentPages++;
                            }else{
                                currentPages=2;
                            }
                        }
                        return;
                    }
                }
            }
        }
        if(adapter==null){
            showEmptyInfo();
        }else {
            recyclerView.setLoadMoreDataComplete("没有更多数据了");
        }
    }

    private void getJSONDatas(JSONArray dataArray,List<BallQGoBettingRecordEntity>datas){
        int size=dataArray.size();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat md=new SimpleDateFormat("MM-dd");
        for(int i=0;i<size;i++){
            BallQGoBettingRecordEntity info=dataArray.getObject(i,BallQGoBettingRecordEntity.class);
            String GMTDate=info.getCtime();
            if (GMTDate.length() > 20)
            {
                GMTDate = GMTDate.substring(0, 19) + "Z";
            }
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            try {
                final Date d = sdf.parse(GMTDate);
                info.setBetting_time(md.format(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            datas.add(info);
        }
    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity) {
        super.userLogin(userInfoEntity);
        showLoading();
        requestUserInfo();
    }
}
