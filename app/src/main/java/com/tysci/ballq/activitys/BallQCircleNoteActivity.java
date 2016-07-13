package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/13.
 */
public class BallQCircleNoteActivity extends BaseActivity{
    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_circle_note;
    }

    @Override
    protected void initViews() {
        setTitle("圈子");
        showLoading();
        requestCircleSectionList();
    }

    @Override
    protected View getLoadingTargetView() {
        return findViewById(R.id.layout_content);
    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    private void requestCircleSectionList(){
        String url= HttpUrls.CIRCLE_HOST_URL+"bbs/section/list";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 30, new HttpClientUtil.StringResponseCallBack() {
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
                hideLoad();
            }
        });
    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle outState) {

    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }
}
