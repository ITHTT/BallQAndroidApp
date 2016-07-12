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
public class UserMessageFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment{
    private int type=0;

    @Override
    protected void onLoadMoreData() {

    }

    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {

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

    private void requestDatas(int type,int pages,final boolean isLoadMore) {
        String url = HttpUrls.HOST_URL_V5 + "user/notification/?etype=" + type + "&p=" + pages;
        HashMap<String, String> params = new HashMap<>(2);
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

    public void setType(int type) {
        this.type = type;
    }
}
