package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/7/15.
 */
public class BallQGoRankListFragment extends BaseFragment{
    private int currentPage=1;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_go_rank_list;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        requestRankInfo(1,false);

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
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

    private void requestRankInfo(int pages,boolean isLoadMore){
        String url= HttpUrls.HOST_URL_V5 + "go/ranklist/?p=" + pages;
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        int status=obj.getIntValue("status");
                        if(status==0){
                            JSONArray dataArrays=obj.getJSONArray("data");
                            if(dataArrays!=null&&!dataArrays.isEmpty()){

                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }
}
