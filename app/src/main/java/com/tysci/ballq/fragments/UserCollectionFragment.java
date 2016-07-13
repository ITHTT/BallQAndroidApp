package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQUserCollectionEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQUserCollectionRecordAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/24.
 */
public class UserCollectionFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment {
    private int type=0;
    private List<BallQUserCollectionEntity> collectionEntityList=null;
    private BallQUserCollectionRecordAdapter adapter=null;

    @Override
    protected void onLoadMoreData() {
        requestDatas(currentPages,true);

    }

    @Override
    protected void onRefreshData() {
        requestDatas(1,false);
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        showLoading();
        requestDatas(1,false);

    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
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

    private void requestDatas(final int pages, final boolean isLoadMore){
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
                if (!isLoadMore) {
                    if (adapter != null) {
                        recyclerView.setStartLoadMore();
                    } else {
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestDatas(pages, isLoadMore);
                            }
                        });
                    }
                } else {
                    recyclerView.setLoadMoreDataFailed();
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        JSONArray datas=obj.getJSONArray("data");
                        if(datas!=null&&!datas.isEmpty()){
                            hideLoad();
                            if(collectionEntityList==null){
                                collectionEntityList=new ArrayList<BallQUserCollectionEntity>(10);
                            }
                            if(!isLoadMore&&!collectionEntityList.isEmpty()){
                                collectionEntityList.clear();
                            }
                            CommonUtils.getJSONListObject(datas,collectionEntityList,BallQUserCollectionEntity.class);
                            if(adapter==null){
                                adapter=new BallQUserCollectionRecordAdapter(collectionEntityList);
                                recyclerView.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                            if (datas.size() < 10) {
                                recyclerView.setLoadMoreDataComplete("没有更多数据了...");
                            } else {
                                recyclerView.setStartLoadMore();
                                if (isLoadMore) {
                                    currentPages++;
                                } else {
                                    currentPages = 2;
                                }
                            }
                            return;
                        }
                    }
                }
                if (!isLoadMore) {
                    if (adapter == null) {
                        showEmptyInfo();
                    }
                } else {
                    recyclerView.setLoadMoreDataComplete("没有更多数据了...");
                }
            }

            @Override
            public void onFinish(Call call) {
                if (!isLoadMore) {
                    if (recyclerView != null) {
                        recyclerView.setRefreshComplete();
                    }
                    onRefreshCompelete();
                }
            }
        });
    }
}
