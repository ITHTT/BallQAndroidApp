package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQMatchBettingScaleEntity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQMatchBettingScaleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/8.
 * 投注比例
 */
public class BallQMatchBettingScaleDataFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment{
    private BallQMatchEntity matchEntity=null;

    private List<BallQMatchBettingScaleEntity> matchBettingScaleEntityList=null;
    private BallQMatchBettingScaleAdapter adapter=null;
    private String loadFinishedTip="没有更多数据了...";

    @Override
    protected void onLoadMoreData() {

    }

    @Override
    protected void onRefreshData() {
        requestBettingScaleDatas(matchEntity.getEid(),matchEntity.getEtype());
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        Bundle data = getArguments();
        if (data != null) {
            matchEntity = data.getParcelable("match_data");
        }
        if(UserInfoUtil.checkLogin(getContext())) {
            if (matchEntity != null) {
                showLoading();
                requestBettingScaleDatas(matchEntity.getEid(), matchEntity.getEtype());
            }
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

    private void requestBettingScaleDatas(final int matchId, final int etype){
        String url= HttpUrls.HOST_URL_V5+"match/" + matchId + "/ostats/?etype=" + etype;
        HashMap<String,String> params=null;
        if(UserInfoUtil.checkLogin(baseActivity)){
            params=new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(baseActivity));
            params.put("token", UserInfoUtil.getUserToken(baseActivity));
        }

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(matchBettingScaleEntityList==null||matchBettingScaleEntityList.isEmpty()){
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestBettingScaleDatas(matchId,etype);
                        }
                    });
                }else{
                    ToastUtil.show(baseActivity, "请求失败");
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        KLog.e("解析JSON字符串....");
                        int status=obj.getIntValue("status");
                        if(status==0){
                            JSONObject dataObj=obj.getJSONObject("data");
                            if(dataObj!=null&&!dataObj.isEmpty()){
                                hideLoad();
                                KLog.e("获取数据Data的JSON对象");
                                List<BallQMatchBettingScaleEntity> ahcs=getBettingTypeDatas(dataObj,"AHC");
                                List<BallQMatchBettingScaleEntity>tos=getBettingTypeDatas(dataObj, "TO");
                                List<BallQMatchBettingScaleEntity>ws=getBettingTypeDatas(dataObj, "3W");
                                List<BallQMatchBettingScaleEntity>hcs=getBettingTypeDatas(dataObj,"HC");
                                //KLog.e("ahcs:"+ahcs.size());
                                if(ahcs!=null||tos!=null||ws!=null||hcs!=null){
                                    KLog.e("获取数据投注比例数据...");
                                    if(matchBettingScaleEntityList==null){
                                        matchBettingScaleEntityList=new ArrayList<BallQMatchBettingScaleEntity>(10);
                                    }

                                    if(!matchBettingScaleEntityList.isEmpty()){
                                        matchBettingScaleEntityList.clear();
                                    }

                                    if(ahcs!=null&&!ahcs.isEmpty()){
                                        matchBettingScaleEntityList.addAll(ahcs);
                                    }

                                    if(tos!=null&&!tos.isEmpty()){
                                        matchBettingScaleEntityList.addAll(tos);
                                    }

                                    if(ws!=null&&!ws.isEmpty()){
                                        matchBettingScaleEntityList.addAll(ws);
                                    }

                                    if(hcs!=null&&!hcs.isEmpty()){
                                        matchBettingScaleEntityList.addAll(hcs);
                                    }

                                    if(adapter==null){
                                        adapter=new BallQMatchBettingScaleAdapter(matchBettingScaleEntityList);
                                        recyclerView.setAdapter(adapter);
                                    }else{
                                        adapter.notifyDataSetChanged();
                                    }
                                    recyclerView.setLoadMoreDataComplete(loadFinishedTip);
                                    return;
                                }
                            }
                        }else{
                            showEmptyInfo(obj.getString("message"));
                            return;
                        }
                    }
                }
                showEmptyInfo();
            }

            @Override
            public void onFinish(Call call) {
                onRefreshCompelete();
            }
        });
    }

    private List getBettingTypeDatas(JSONObject dataObj,String type){
        JSONArray dataArray=dataObj.getJSONArray(type);
        if(dataArray!=null&&!dataArray.isEmpty()){
            ArrayList<BallQMatchBettingScaleEntity> datas=new ArrayList<>();
            CommonUtils.getJSONListObject(dataArray, datas, BallQMatchBettingScaleEntity.class);
            setBettingType(datas, type);
            return datas;
        }
        return null;
    }

    private void setBettingType(List<BallQMatchBettingScaleEntity>datas,String type){
        if(datas!=null&&!datas.isEmpty()){
            for(BallQMatchBettingScaleEntity info:datas){
                info.setBettingType(type);
            }
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

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity) {
        super.userLogin(userInfoEntity);
        if (matchEntity != null) {
            HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
            showLoading();
            requestBettingScaleDatas(matchEntity.getEid(), matchEntity.getEtype());
        }
    }

    @Override
    protected void userExit() {
        super.userExit();
        showEmptyInfo("您尚未登录,登录后才可查看", "点击登录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoUtil.userLogin(baseActivity);
            }
        });
    }
}
