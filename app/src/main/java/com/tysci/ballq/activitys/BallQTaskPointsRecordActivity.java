package com.tysci.ballq.activitys;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQTaskPointsRecordEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQTaskPointsRecordAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/6.
 */
public class BallQTaskPointsRecordActivity extends BaseActivity {
    @Bind(R.id.ivRewardGoldCoinByCompleteIntegralTask1)
    protected ImageView ivTaskGold01;
    @Bind(R.id.tvRewardGoldCoinByCompleteIntegralTask1)
    protected TextView tvTaskGold01;
    @Bind(R.id.tvRewardGoldCoinByCompleteIntegralTask2)
    protected TextView tvTaskGold02;
    @Bind(R.id.ivRewardGoldCoinByCompleteIntegralTask2)
    protected ImageView ivTaskGold02;
    @Bind(R.id.task_progress)
    protected ProgressBar taskProgress;
    @Bind(R.id.rv_task_records)
    protected RecyclerView rvTaskRecords;


    private List<BallQTaskPointsRecordEntity> taskPointsRecordEntityList=null;
    private BallQTaskPointsRecordAdapter adapter=null;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_task_points;
    }

    @Override
    protected void initViews() {
        setTitle("任务积分");
        rvTaskRecords.setLayoutManager(new LinearLayoutManager(this));
        taskProgress.setMax(50);
        taskProgress.setProgress(0);
        requestPointsTaskRecordsInfo();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    private void requestPointsTaskRecordsInfo(){
        String url= HttpUrls.HOST_URL_V5+"user/point_mission/";
        HashMap<String,String>params=new HashMap<>(2);
        params.put("user", UserInfoUtil.getUserId(this));
        params.put("token", UserInfoUtil.getUserToken(this));
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag,url,params, new HttpClientUtil.StringResponseCallBack() {
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
                        JSONObject data=obj.getJSONObject("data");
                        if(data!=null&&!data.isEmpty()){
                            taskProgress.setProgress(data.getIntValue("point"));
                            setTaskProgressState();
                            JSONArray datas=data.getJSONArray("mission");
                            if(datas!=null&&!datas.isEmpty()){
                                if(taskPointsRecordEntityList==null){
                                    taskPointsRecordEntityList=new ArrayList<BallQTaskPointsRecordEntity>(10);
                                }
                                CommonUtils.getJSONListObject(datas,taskPointsRecordEntityList,BallQTaskPointsRecordEntity.class);
                                if(adapter==null){
                                    adapter=new BallQTaskPointsRecordAdapter(taskPointsRecordEntityList);
                                    rvTaskRecords.setAdapter(adapter);
                                }
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

    private void setTaskProgressState(){
        int progress=taskProgress.getProgress();
        Resources res = getResources();

        if (progress >= 20) {
            ivTaskGold01.setSelected(true);
            tvTaskGold01.setTextColor(res.getColor(R.color.gold));
        } else {
            ivTaskGold01.setSelected(false);
            tvTaskGold01.setTextColor(Color.parseColor("#797979"));
        }
        if (progress >= 50) {
            ivTaskGold02.setSelected(true);
            tvTaskGold02.setTextColor(res.getColor(R.color.gold));
        } else {
            ivTaskGold02.setSelected(false);
            tvTaskGold02.setTextColor(Color.parseColor("#797979"));
        }
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
