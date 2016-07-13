package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.UserAttentionActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQUserAttentionOrFansEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.UserAttentionOrFansAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/23.
 */
public class UserAttentionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.tv_push)
    protected TextView tvPush;
    @Bind(R.id.tv_attention)
    protected TextView tvAttention;
    @Bind(R.id.divider)
    protected View divider;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;

    private List<BallQUserAttentionOrFansEntity> userAttentionOrFansEntityList;
    private UserAttentionOrFansAdapter adapter=null;

    private int etype;
    private String uid;
    private int currentPages=1;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_user_attention_or_fans;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        recyclerView.setOnLoadMoreListener(this);
        swipeRefresh.setOnRefreshListener(this);
        showLoading();
        requestDatas(1, false);
        setTitleAttrs();
    }

    private void setTitleAttrs(){
        if(etype==0){
            tvPush.setVisibility(View.GONE);
            tvAttention.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }else{
            tvPush.setVisibility(View.VISIBLE);
            tvAttention.setVisibility(View.VISIBLE);
            String id=UserInfoUtil.getUserId(baseActivity);
            if(!uid.equals(id)){
                tvPush.setVisibility(View.GONE);
                tvAttention.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
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
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                        // recyclerView.setStartLoadMore();
                    }
                }
            }, 1000);
        }
    }


    private void requestDatas(final int pages, final boolean isLoadMore){
        String url= HttpUrls.HOST_URL_V5 + "user/follow/?etype=" + etype + "&uid=" + uid + "&p=" + pages;
        Map<String,String> params=null;
        if(UserInfoUtil.checkLogin(baseActivity)){
            params=new Hashtable<>(2);
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
                        ToastUtil.show(baseActivity,"请求失败");
                        recyclerView.setStartLoadMore();
                    } else {
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestDatas(pages,isLoadMore);
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
                        JSONArray arrays=obj.getJSONArray("data");
                        if(arrays!=null&&!arrays.isEmpty()){
                            hideLoad();
                            publishLoadDataEvent(arrays.size());
                            if(userAttentionOrFansEntityList==null){
                                userAttentionOrFansEntityList=new ArrayList<BallQUserAttentionOrFansEntity>(10);
                            }
                            if(!isLoadMore){
                                if(!userAttentionOrFansEntityList.isEmpty()){
                                    userAttentionOrFansEntityList.clear();
                                }
                            }
                            CommonUtils.getJSONListObject(arrays,userAttentionOrFansEntityList,BallQUserAttentionOrFansEntity.class);
                            if(adapter==null){
                                adapter=new UserAttentionOrFansAdapter(userAttentionOrFansEntityList);
                                if(etype==1){
                                    String id = UserInfoUtil.getUserId(baseActivity);
                                    if (uid.equals(id)) {
                                        adapter.setIsSelf(true);
                                    } else {
                                        adapter.setIsSelf(false);
                                    }
                                }
                                recyclerView.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                            if(arrays.size()<10){
                                recyclerView.setLoadMoreDataComplete();
                            }else{
                                recyclerView.setStartLoadMore();
                                if(isLoadMore){
                                    currentPages++;
                                }else{
                                    currentPages=2;
                                }
                            }
                            return;
                        }
                    }
                }
                if (!isLoadMore) {
                    recyclerView.setLoadMoreDataComplete();
                    showEmptyInfo();
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

    public void setEtype(int etype) {
        this.etype = etype;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private void publishLoadDataEvent(int count){
        EventObject eventObject=new EventObject();
        eventObject.addReceiver(UserAttentionActivity.class);
        eventObject.getData().putInt("etype", etype);
        eventObject.getData().putInt("count",count);
        EventObject.postEventObject(eventObject,"user_attention");
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
    public void onLoadMore() {
        if(recyclerView.isRefreshing()){
            recyclerView.setLoadMoreDataComplete("刷新数据中...");
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
    public void onRefresh() {
        if(recyclerView.isLoadMoreing()){
            recyclerView.setRefreshing();
            onRefreshCompelete();
        }else{
            requestDatas(1,false);
        }
    }
}
