package com.tysci.ballq.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQGreatWarGoActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQAuthorAnalystsEntity;
import com.tysci.ballq.modles.BallQBannerImageEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQBusinessControler;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.widgets.BallQUserAnalystView;
import com.tysci.ballq.views.widgets.BannerNetworkImageView;
import com.tysci.ballq.views.widgets.TitleBar;
import com.tysci.ballq.views.widgets.convenientbanner.ConvenientBanner;
import com.tysci.ballq.views.widgets.convenientbanner.holder.CBViewHolderCreator;
import com.tysci.ballq.views.widgets.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQIndexPageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnItemClickListener{
    @Bind(R.id.title_bar)
    protected TitleBar titleBar;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.convenientBanner)
    protected ConvenientBanner banner;
    @Bind(R.id.iv_fight_ballq_gou)
    protected ImageView menuFightGou;
    @Bind(R.id.iv_get_ballq_gold)
    protected ImageView menuGetGold;
    @Bind(R.id.first_analyst)
    protected BallQUserAnalystView firstAnalyst;
    @Bind(R.id.second_analyst)
    protected BallQUserAnalystView secondAnalyst;
    @Bind(R.id.third_analyst)
    protected BallQUserAnalystView thirdAnalyst;

    private List<BallQBannerImageEntity> bannerImageEntityList=null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_index;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        titleBar.setTitleBarTitle("首页");
        titleBar.setTitleBarLeftIcon(0, null);
        banner.setBackgroundColor(Color.parseColor("#f6f6f6"));
        banner.setOnItemClickListener(this);
        banner.getViewPager().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (swipeRefresh != null)
                            swipeRefresh.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (swipeRefresh != null) {
                            swipeRefresh.setEnabled(true);
                        }
                }
                return false;
            }
        });
        swipeRefresh.setOnRefreshListener(this);
        showLoading();
        getHomePageInfo();

    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    private void setRefreshing() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void onRefreshCompelete() {
        swipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        }, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        banner.startTurning(3000);
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

    private void getHomePageInfo() {
        String url = HttpUrls.HOST_URL_V5 + "home/";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(bannerImageEntityList==null){
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            getHomePageInfo();
                        }
                    });
                }else{

                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()&&obj.getIntValue("status")==0){
                        hideLoad();
                        JSONObject dataObj=obj.getJSONObject("data");
                        if(dataObj!=null&&!dataObj.isEmpty()){
                            JSONArray picArray=dataObj.getJSONArray("pics");
                            if(picArray!=null&&!picArray.isEmpty()){
                                setBannerPictures(picArray);
                            }
                            JSONArray recommdArray=dataObj.getJSONArray("recomend");
                            if(recommdArray!=null&&!recommdArray.isEmpty()){
                                setAuthorAnalystInfo(recommdArray);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                onRefreshCompelete();
            }
        });
    }

    private void setBannerPictures(JSONArray picArray){
        if(bannerImageEntityList==null||bannerImageEntityList.isEmpty()){
            if(bannerImageEntityList==null){
                bannerImageEntityList=new ArrayList<>(picArray.size());
            }
            CommonUtils.getJSONListObject(picArray,bannerImageEntityList,BallQBannerImageEntity.class);
            banner.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new BannerNetworkImageView();
                }
            }, bannerImageEntityList)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可不设
                    .setPageIndicator(new int[]{R.drawable.guide_gray, R.drawable.guide_red});
            banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
            banner.setPointViewVisible(true);
            banner.setManualPageable(true);
        }else{
            bannerImageEntityList.clear();
            CommonUtils.getJSONListObject(picArray,bannerImageEntityList,BallQBannerImageEntity.class);
            banner.notifyDataSetChanged();
        }
    }

    private void setAuthorAnalystInfo(JSONArray authorArray){
        if(authorArray!=null&&!authorArray.isEmpty()){
            int size=authorArray.size();
            for(int i=0;i<size;i++){
                BallQAuthorAnalystsEntity info=authorArray.getObject(i,BallQAuthorAnalystsEntity.class);
                if(info!=null){
                    if(i==0){
                        firstAnalyst.setBallQAuthorAnalystsInfo(info);
                        firstAnalyst.setVisibility(View.VISIBLE);
                        secondAnalyst.setVisibility(View.GONE);
                        secondAnalyst.setVisibility(View.GONE);
                    }else if(i==1){
                        secondAnalyst.setBallQAuthorAnalystsInfo(info);
                        secondAnalyst.setVisibility(View.VISIBLE);
                    }else if(i==2){
                        thirdAnalyst.setBallQAuthorAnalystsInfo(info);
                        thirdAnalyst.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        getHomePageInfo();
    }

    @Override
    public void onItemClick(int position) {
        if(bannerImageEntityList!=null){
            BallQBannerImageEntity info=bannerImageEntityList.get(position);
            BallQBusinessControler.businessControler(baseActivity,Integer.parseInt(info.getJump_type()),info.getJump_url());
        }
    }

    @OnClick({R.id.iv_fight_ballq_gou,R.id.iv_get_ballq_gold})
    protected void onClickBallQEvents(View view){
        int id=view.getId();
        Intent intent=null;
        switch(id){
            case R.id.iv_fight_ballq_gou:
                intent=new Intent(baseActivity, BallQGreatWarGoActivity.class);
                break;
            case R.id.iv_get_ballq_gold:
                break;
        }
        if(intent!=null){
            startActivity(intent);
        }
    }
}
