package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.view.View;

import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/24.
 */
public class UserCollectionFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment {
    private int type=0;

    @Override
    protected void onLoadMoreData() {

    }

    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        requestDatas(1,false);

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

    public void setType(int type) {
        this.type = type;
    }

    private void requestDatas(int pages,boolean isLoadMore){
        String url= HttpUrls.HOST_URL_V5 + "user/favorites/?p=" + pages;
        HashMap<String,String> params=new HashMap<>(3);
        params.put("etype",String.valueOf(type));
        if (UserInfoUtil.checkLogin(baseActivity)) {
            params.put("user", UserInfoUtil.getUserId(baseActivity));
            params.put("token", UserInfoUtil.getUserToken(baseActivity));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);

            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }
}
